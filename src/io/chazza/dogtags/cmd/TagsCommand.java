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
    public Inventory getTagInventory(Player p){

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(DogTags.getInstance().getDataFolder(), "config.yml"));
        Integer size;
        if(config.getBoolean("gui.dynamic")){
            int available = 0;
            for(DogTag tag : DogTags.getTags()){
                if(!hasPermission(p, tag) && !config.getBoolean("gui.show-no-access")) continue;
                available++;
            }

            size = ((((available / 9) + ((available % 9 == 0) ? 0 : 1)) * 9));

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

            if(dt != null && dt.getId() == tag.getId()){
                is.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                ItemMeta meta = is.getItemMeta();

                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                is.setItemMeta(meta);
            }

            inv.addItem(is);

        }
        inv.setTitle(ColorUtil.translate(config.getString("gui.name")
            .replace("%total%", ""+available)));
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
                    return;
                }
            }
        }
    }
}
