package io.chazza.dogtags;

import co.aikar.commands.ACF;
import co.aikar.commands.CommandManager;
import io.chazza.dogtags.cmd.*;
import io.chazza.dogtags.dev.DogTag;
import io.chazza.dogtags.hook.PlaceholderAPIHook;
import io.chazza.dogtags.manager.ConfigManager;
import io.chazza.dogtags.util.LogUtil;
import io.chazza.dogtags.util.MySQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

/**
 * Created by charliej on 31/05/2017.
 */
public class DogTags extends JavaPlugin {

    private static DogTags instance;
    public static DogTags getInstance(){
        return instance;
    }

    private static Enum<StorageEnum> storage;
    private static MySQLConnection mySQLConnection;
    public static MySQLConnection getConnection(){
        return mySQLConnection;
    }
    public static Enum<StorageEnum> getStorage(){
        return storage;
    }


    public static List<DogTag> tags;
    public static List<DogTag> getTags(){
        return tags;
    }

    public static DogTag getTag(String tag){
        for(DogTag dt : tags){
            if(dt.getId().equalsIgnoreCase(tag)){
                return dt;
            }
        }
        return null;
    }
    public static void addTag(DogTag dt){
        getTags().add(dt);
    }
    public static void removeTag(String tag){
        for(DogTag dt : tags){
            if(dt.getId().equalsIgnoreCase(tag)){
                getTags().remove(dt);
                return;
            }
        }
    }
    public void handleReload(){
        ConfigManager.load();
        StorageHandler.registerTags();
    }

    public void onEnable(){
        LogUtil.outputMsg("Setting up..");
        instance = this;
        try {
            ConfigManager.create();
        } catch (IOException e) {
            e.printStackTrace();
        }


        LogUtil.outputMsg("= Registering Commands & Aliases =");
        CommandManager cm = ACF.createManager(this);
        String cmd = "dogtags|dogtag|tags|tag|dtags|dtag";
        cm.getCommandReplacements().addReplacement("%command%", cmd);
        cm.registerCommand(new TagsCommand());
        cm.registerCommand(new HelpCommand());
        cm.registerCommand(new InfoCommand());

        cm.registerCommand(new CreateCommand());
        cm.registerCommand(new RemoveCommand());
        cm.registerCommand(new PermCommand());
        cm.registerCommand(new ReloadCommand());
        cm.registerCommand(new SetCommand());
        cm.registerCommand(new ClearCommand());
        cm.registerCommand(new ListCommand());
        cm.registerCommand(new ConvertCommand());

        LogUtil.outputMsg("= Registering Tags =");
        if(getConfig().getString("storage.type").equalsIgnoreCase("FLATFILE")){
            storage = StorageEnum.FLATFILE;
            LogUtil.outputMsg("DogTags will use FlatFile for storage.");
        }else{
            storage = StorageEnum.MYSQL;
            try {
                mySQLConnection = new MySQLConnection(getConfig().getString("storage.host"),
                    getConfig().getString("storage.database"),
                    getConfig().getString("storage.username"),
                    getConfig().getString("storage.password"),
                    getConfig().getInt("storage.port"));
                mySQLConnection.testConnection();
            } catch (Exception e) {e.printStackTrace();}
            LogUtil.outputMsg("DogTags will use MySQL for storage.");
        }

        StorageHandler.registerTags();

        LogUtil.outputMsg("= Registering Events =");
        Bukkit.getPluginManager().registerEvents(new TagsCommand(), DogTags.getInstance());

        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            LogUtil.outputMsg("Hooked into PlaceholderAPI.");
            new PlaceholderAPIHook(this).hook();
        }

    }

    public void onDisable(){
        // Save Shit
    }

}
