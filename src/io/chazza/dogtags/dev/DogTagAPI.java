package io.chazza.dogtags.dev;

import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import org.bukkit.entity.Player;

/**
 * Created by charliej on 01/06/2017.
 */
public class DogTagAPI {

    public static String getTagPrefix(String id){
        DogTag dt = DogTags.getTag(id);
        return dt.getPrefix();
    }
    public static String getTagDescription(String id){
        DogTag dt = DogTags.getTag(id);
        return dt.getDescription();
    }
    public static void setUserTag(Player p, String id){
        StorageHandler.setUser(p, id);
    }
}
