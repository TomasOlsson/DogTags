package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.dev.DogTag;
import io.chazza.dogtags.dev.DTSelectEvent;
import io.chazza.dogtags.manager.ConfigManager;
import io.chazza.dogtags.util.ColorUtil;
import io.chazza.dogtags.util.InventoryUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by charliej on 01/06/2017.
 */
public class TagsCommand extends BaseCommand implements Listener {

    private boolean hasPermission(Player p, DogTag dt){
        if(!dt.hasPermission()){
            return true;
        }else if(dt.hasPermission() && p.hasPermission("dogtags.use."+dt.getId())) {
            return true;
        }
        return false;
    }

    private ItemStack getRemoveItem(Player p){
        ConfigManager.load();
        FileConfiguration config = ConfigManager.get();

        Material mat = config.getString("gui.gui-item.item") != null ?
            Material.valueOf(config.getString("gui.gui-item.item")) : Material.PAPER;

        ItemStack is = new ItemStack(mat);
        is.setDurability(Short.valueOf(ConfigManager.getInt("gui.gui-item.data")+""));
        ItemMeta im = is.getItemMeta();

        ArrayList<String> lore = new ArrayList<>();

        String title = ColorUtil.translate(ConfigManager.getString("gui.gui-item.name"));
        String id = StorageHandler.getPlayerTag(p) != null ? StorageHandler.getPlayerTag(p) : "No";
        title = title.replace("%id%", WordUtils.capitalizeFully(id.toLowerCase()));

        im.setDisplayName(title);

        for(String l : config.getStringList("gui.gui-item.lore")){
            lore.add(ColorUtil.translate(l));
        }
        im.setLore(lore);

        is.setItemMeta(im);

        return is;
    }
    private Inventory getTagInventory(Player p){
        ConfigManager.load();
        FileConfiguration config = ConfigManager.get();

        Integer size;
        if(config.getBoolean("gui.dynamic")){
            int available = 0;
            for(DogTag tag : DogTags.getTags()){
                if(!hasPermission(p, tag) && !config.getBoolean("gui.show-no-access")) continue;
                available++;
            }

            size = ((((available / 9) + ((available % 9 == 0) ? 0 : 1)) * 9));
            if(size < 54) size = size + 9;

        }else size = config.getInt("gui.size");

        InventoryUtil inv = new InventoryUtil(p, ColorUtil.translate(config.getString("gui.name")
            .replace("%total%", ""+DogTags.getTags().size())), size);


        int available = 0;
        for(DogTag tag : DogTags.getTags()){
            ItemStack is = new ItemStack(Material.valueOf(config.getString("gui.item")));
            is.setType(tag.getItem().getType());
            is.setDurability(tag.getItem().getDurability());
            ItemMeta im = is.getItemMeta();

            im.setDisplayName(ColorUtil.translate("§f"+config.getString("gui.format.name")
            .replace("%id%", tag.getId()).replace("%tag%", tag.getPrefix())));

            ArrayList<String> lore = new ArrayList<>();

            String permission = hasPermission(p, tag) ? config.getString("replacement.permission.true") : config.getString("replacement.permission.false");
            if(!hasPermission(p, tag) && !config.getBoolean("gui.show-no-access")) continue;

            available++;

            for(String l : config.getStringList("gui.format.lore")){
                lore.add(ColorUtil.translate(l)
                    .replace("%description%", tag.getDescription())
                .replace("%permission%", ColorUtil.translate(permission)));
            }

            im.setLore(lore);

            is.setItemMeta(im);

            String storageTag = StorageHandler.getPlayerTag(p);

            String tagstr = storageTag != null ? storageTag : "";

            DogTag dt = DogTags.getTag(tagstr);

            if(dt != null && dt.getId().equalsIgnoreCase(tag.getId())){
                is.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                ItemMeta meta = is.getItemMeta();

                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                is.setItemMeta(meta);
            }

            inv.addItem(is);

        }
        inv.setTitle(ColorUtil.translate(config.getString("gui.name")
            .replace("%total%", ""+available)));

        // CURRENT TAG //
        if(ConfigManager.getBoolean("gui.gui-item.enabled") && StorageHandler.getPlayerTag(p) != null) {
            inv.setItem(getRemoveItem(p), size - ConfigManager.getInt("gui.gui-item.slot"));
        }

        return inv.getInventory();

    }

    @CommandAlias("%command%")
    public void onCommand(Player p){
        if(!p.hasPermission("dogtags.gui")) { p.sendMessage(TagLang.NO_PERMISSION.get()); return; }
        p.openInventory(getTagInventory(p));
    }

    @EventHandler(ignoreCancelled = true)
    public void onInvClick(InventoryClickEvent e){
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
        if(inv == null) return;

        if(e.getCurrentItem() == null) return;


        Inventory tagGUI = getTagInventory(p);

        if(inv.getHolder() == tagGUI.getHolder()){
                if(inv.getTitle().equalsIgnoreCase(tagGUI.getTitle())){
                e.setCancelled(true);

                if(e.getCurrentItem().isSimilar(getRemoveItem(p))){
                    p.sendMessage(TagLang.CLEARED.get());
                    p.closeInventory();
                    StorageHandler.clearPlayerTag(p);
                    return;
                }

                if(e.getSlot() > DogTags.getTags().size()) return;

                DogTag dt = DogTags.getTags().get(e.getSlot());

                if(!hasPermission(p, dt)){
                    return;
                }
                if(e.isLeftClick() || e.isRightClick()) {

                    DTSelectEvent selectEvent = new DTSelectEvent(p, dt.getId());
                    Bukkit.getPluginManager().callEvent(selectEvent);
                    if(selectEvent.isCancelled()) return;

                    p.sendMessage(TagLang.SET.get().replace("%id%", dt.getId()).replace("%prefix%", dt.getPrefix()));
                    StorageHandler.setUser(p, dt.getId());

                    p.closeInventory();
                }
            }
        }
    }
}
