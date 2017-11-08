package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class PermCommand extends BaseCommand implements Listener {

    @Subcommand("perm|permission") @Syntax("<tag>")
    public void onCommand(CommandSender sender, String ID){
    	if(!sender.hasPermission("dogtags.perm")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }
    		if(DogTags.getTag(ID) != null){
    			if (StorageHandler.getPerm(ID) == false) { StorageHandler.setPerm(ID, true); sender.sendMessage(TagLang.TAG_PERMISSION.get()
    		               .replace("%id%", ID)
    		               .replace("%permission%", "dogtags.use."+ID.toLowerCase())); }
    			else { StorageHandler.setPerm(ID, false); sender.sendMessage(TagLang.TAG_NO_PERMISSION.get().replace("%id%", ID)); }
    			
    		}else {
    			sender.sendMessage(TagLang.INVALID_TAG.get());
    			return;
    	}
    }

}
