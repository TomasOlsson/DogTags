package io.chazza.dogtags.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryUtil {

    private Inventory inv;
    private InventoryHolder ih;
    private String name;
    private Integer size;
    private HashMap<ItemStack, Integer> items;
    private int count;
   
    
    public InventoryUtil(Player p, String name, Integer size){
        this.ih = p;
        this.name = name;
        this.size = size;
        items = new HashMap<>();
        count = 0;
    }
    
    public void addItem(ItemStack is){
        items.put(is, count);
        count++;
    }
    public void setItem(ItemStack is, Integer slot){
        items.put(is, slot);
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
        for (Map.Entry<ItemStack, Integer> mp : items.entrySet()) {
            inv.setItem(mp.getValue(), mp.getKey());
        }
        return inv;
    }
    public InventoryHolder getHolder(){
        return ih;
    }
}
