package io.chazza.dogtags.util;

import org.bukkit.Bukkit;

/**
 * Created by charliej on 01/06/2017.
 */
public class LogUtil {

    public static void outputMsg(String msg){
        Bukkit.getLogger().info("[DogTags] "+msg);
    }
}
