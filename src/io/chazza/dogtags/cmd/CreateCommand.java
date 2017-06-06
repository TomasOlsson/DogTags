package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.dev.DogTag;
import io.chazza.dogtags.manager.ConfigManager;
import io.chazza.dogtags.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class CreateCommand extends BaseCommand implements Listener {

    @Subcommand("create") @Syntax("<tag> [prefix] [description]")
    public void onCommand(CommandSender sender, String ID, @Optional @Single String prefix, @Optional String description){
        if(!sender.hasPermission("dogtags.create")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

       if(ConfigManager.get().getString("dogtags."+ID) == null){
           prefix = prefix != null ? ColorUtil.translate(prefix) : "§8[§e§l"+ID+"§8]";
           description = description != null ? ColorUtil.translate(description) : "§7Default Description!";

           StorageHandler.addTag(ID, prefix, description, true);
           DogTag dogTag = new DogTag(ID);
           dogTag.withDescription(description).withPrefix(prefix).withPermission(true).build(DogTags.getInstance());

           sender.sendMessage(TagLang.CREATED.get().replace("%id%", ID).replace("%prefix%", prefix));
           return;
       }else {
           sender.sendMessage(TagLang.ALREADY_EXISTS.get());
           return;
       }
    }

}
