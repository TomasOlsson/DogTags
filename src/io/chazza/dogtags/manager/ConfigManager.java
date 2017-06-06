package io.chazza.dogtags.manager;


import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.util.ColorUtil;
import io.chazza.dogtags.util.LogUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static File f;
    private static FileConfiguration fc;
    public static FileConfiguration get(){
        return fc;
    }

    public static boolean load(){
        fc = new YamlConfiguration();
        try{
            fc.load(f);
            return true;
        }catch (IOException | InvalidConfigurationException e) {
            LogUtil.outputMsg("Error, "+e.getLocalizedMessage()+".");
            return false;
        }
    }
    public static void create() throws IOException{
        File pluginFolder = DogTags.getInstance().getDataFolder();
        f = new File(pluginFolder, "config.yml");

        if(!f.exists()){
            f.getParentFile().mkdirs();
            DogTags.getInstance().saveResource(f.getName(), false);
            LogUtil.outputMsg("File &6%file% &fdoesn't exist, creating file!".replace("%file%", f.getName()));
        }
        fc = new YamlConfiguration();
        try {
            fc.load(f);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void reload(){
        if (f == null) {
            f = new File(DogTags.getInstance().getDataFolder(), "config.yml");
            if (!f.exists()) {
                try {
                    create();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fc = YamlConfiguration.loadConfiguration(f);
        }
    }


}
