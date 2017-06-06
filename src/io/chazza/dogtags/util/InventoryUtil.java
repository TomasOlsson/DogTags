package io.chazza.dogtags.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {

    private Inventory inv;
    private InventoryHolder ih;
    private String name;
    private Integer size;
    private List<ItemStack> items;
   
    
    public InventoryUtil(Player p, String name, Integer size){
        this.ih = p;
        this.name = name;
        this.size = size;
        items = new ArrayList<>();
    }
    
    public void addItem(ItemStack is){
        items.add(is);
    }
    public void removeItem(ItemStack is){
        items.remove(is);
    }
    public void setTitle(String title){
        this.name = ColorUtil.translate(title);
    }

    public Integer getSize(){
        return size;
    }
    public String getName(){
        return ColorUtil.translate(name);
    }
    public Inventory getInventory(){
        inv = Bukkit.createInventory(ih, size, name);
        for(ItemStack is : items){
            inv.addItem(is);
        }
        return inv;
    }
    public InventoryHolder getHolder(){
        return ih;
    }
}
