package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.dogtags.DogTags;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import javax.jws.soap.SOAPBinding;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class InfoCommand extends BaseCommand implements Listener {

    @Subcommand("info")
    public void onTagsCommand(CommandSender sender){
        sender.sendMessage("§6[§eDogTags§6] §7Running §ev%version% §7by §6fiver.io§7."
            .replace("%version%", DogTags.getInstance().getDescription().getVersion()));
        sender.sendMessage("§6[§eDogTags§6] §7Developer: §eChazmondo§7.");
        sender.sendMessage("§6[§eDogTags§6] §7Use §6/dogtags help §7for commands.");
    }

}
