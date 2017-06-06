package io.chazza.dogtags.hook;

import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.dev.DogTag;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

/**
 * Created by charliej on 12/04/2017.
 */
public class PlaceholderAPIHook extends EZPlaceholderHook {

    private DogTags plugin;

    public PlaceholderAPIHook(DogTags plugin) {
        // this is the plugin that is registering the placeholder and the identifier for our placeholder.
        // the format for placeholders is this:
        // %<placeholder identifier>_<anything you define as an identifier in your method below>%
        // the placeholder identifier can be anything you want as long as it is not already taken by another
        // registered placeholder.
        super(plugin, "dogtags");
        // this is so we can access our main class below
        this.plugin = plugin;
    }


    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if(identifier.equals("current_tag")){
            String storageTag = StorageHandler.getPlayerTag(p);

            String tag = storageTag != null ? storageTag : "";

            DogTag dt = DogTags.getTag(tag);
            if(dt == null){
                return StringUtils.deleteWhitespace("");
            }else return dt.getPrefix();

        }
        return null;
    }
}
