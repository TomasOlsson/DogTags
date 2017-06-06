package io.chazza.dogtags.dev;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DTSelectEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private Player player;
    private String tag;

    public DTSelectEvent(Player player, String tag){
        this.player = player;
        this.tag = tag;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public String getTag(){
        return tag;
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }
    
    public static HandlerList getHandlerList(){
        return handlers;
    }
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
