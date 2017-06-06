package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.manager.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class RemoveCommand extends BaseCommand implements Listener {

    @Subcommand("remove") @Syntax("<tag>")
    public void onCommand(CommandSender sender, String ID){
        if(!sender.hasPermission("dogtags.remove")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

       if(ConfigManager.get().getString("dogtags."+ID) == null){

           StorageHandler.removeTag(ID);
           DogTags.removeTag(ID);

           sender.sendMessage(TagLang.REMOVED.get().replace("%id%", ID));
           return;
       }else {
           sender.sendMessage(TagLang.INVALID_TAG.get());
           return;
       }
    }

}
