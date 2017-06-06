package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.dev.DogTag;
import org.bukkit.command.CommandSender;

/**
 * Created by charliej on 07/04/2017.
 */
@CommandAlias("%command%")
public class ListCommand extends BaseCommand {

    @Subcommand("list")
    public void onCommand(CommandSender sender){
        if(!sender.hasPermission("dogtags.list")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

        sender.sendMessage(" ");
        sender.sendMessage("§6[§eDogTags§6] §fListing Tags");
        sender.sendMessage(" ");
        for(DogTag dt : DogTags.getTags()) {
            sender.sendMessage(" §e"+dt.getId() + " §7("+dt.getPrefix() + "§7) §6- §f"+dt.getDescription());
        }
        sender.sendMessage(" ");
        sender.sendMessage("§6[§eDogTags§6] §fTotal: §e§l"+DogTags.getTags().size());
        sender.sendMessage(" ");
    }
}
