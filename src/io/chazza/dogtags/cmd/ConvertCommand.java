package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageEnum;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.dev.DogTag;
import io.chazza.dogtags.manager.ConfigManager;
import io.chazza.dogtags.util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("dtconvert")
public class ConvertCommand extends BaseCommand implements Listener {


    @Subcommand("DeluxeTags") @CommandCompletion("deluxetags")
    public void onCommand(CommandSender sender){
        if(!sender.hasPermission("dogtags.convert")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

        File f = new File(DogTags.getInstance().getDataFolder().getParentFile().getPath() + File.separator + "DeluxeTags", "config.yml");
        if(!f.exists()) return;

        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(DogTags.getInstance().getDataFolder(), "config.yml"));

        for(String tags : fc.getConfigurationSection("deluxetags").getKeys(false)){
            String prefix = fc.getString("deluxetags."+tags+".tag");
            String description = fc.getString("deluxetags."+tags+".description");
            if (DogTags.getStorage() == StorageEnum.FLATFILE) {
            	config.set("dogtags."+tags+".prefix", prefix);
            	config.set("dogtags."+tags+".description", description);
            	config.set("dogtags."+tags+".permission", true);
            }else{
                DogTags.getConnection().insertTag(tags, prefix, description, true);
            }
            LogUtil.outputMsg("Converted "+tags+" with prefix "+prefix + " and description "+description);
        }

           // config.save(new File(DogTags.getInstance().getDataFolder(), "config.yml"));
            DogTags.getInstance().handleReload();
        if(sender instanceof Player) sender.sendMessage("§6[§eDogTags§6] §fCheck Console for Information.");
    }
}

