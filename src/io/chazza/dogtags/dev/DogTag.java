package io.chazza.dogtags.dev;

import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.util.ColorUtil;
import io.chazza.dogtags.util.LogUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Created by charliej on 01/06/2017.
 */
public class DogTag {

    private String id, prefix, description;
    private boolean permission;
    private ItemStack item;

    public DogTag(String id){
        this.id = id;
    }

    public DogTag withItem(ItemStack item){
        this.item = item;
        return this;
    }

    public DogTag withPrefix(String prefix){
        this.prefix = ColorUtil.translate(prefix);
        return this;
    }

    public DogTag withDescription(String description){
        this.description = ColorUtil.translate(description);
        return this;
    }

    public DogTag withPermission(boolean permission){
        this.permission = permission;
        return this;
    }

    public void build(Plugin pl){
        String plugin = !pl.getName().equalsIgnoreCase("DogTags") ? pl.getName() : null;

        DogTags.getTags().add(this);

        if(plugin != null){
            LogUtil.outputMsg(pl.getName() + " has registered the '"+id+"' tag.");
        }
    }

    public String getId(){return id;}
    public String getPrefix(){return prefix;}
    public String getDescription(){return description;}
    public boolean hasPermission(){return permission;}
    public ItemStack getItem(){
        return item != null ? item : new ItemStack(Material.NAME_TAG);
    }

    public void setPrefix(String prefix){
        this.prefix = ColorUtil.translate(prefix);
    }
    public void setDescription(String description){
        this.description = ColorUtil.translate(description);
    }
}
