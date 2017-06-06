package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.dogtags.TagLang;
import org.bukkit.command.CommandSender;

/**
 * Created by charliej on 07/04/2017.
 */
@CommandAlias("%command%")
public class HelpCommand extends BaseCommand {

    private String help(String cmd, String info){
        return "§e"+cmd+" §8- §7"+info;
    }

    @Subcommand("help")
    public void onCommand(CommandSender sender){
        if(!sender.hasPermission("dogtags.help")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

        sender.sendMessage(" ");
        sender.sendMessage("§6[§eDogTags§6] §fListing Commands");
        sender.sendMessage(help(" /dogtags", "Shows GUI."));
        sender.sendMessage(help(" /dogtags info", "Plugin Information."));
        sender.sendMessage(help(" /dogtags reload", "Reload Configuration."));
        sender.sendMessage(help(" /dogtags set <player> <id>", "Sets a Tag."));
        sender.sendMessage(help(" /dogtags list", "Shows Available Tags."));
        sender.sendMessage(help(" /dogtags setdesc <id> <description>", "Sets Description."));
        sender.sendMessage(help(" /dogtags setprefix <id> <prefix>", "Sets Prefix."));
        sender.sendMessage(help(" /dogtags create <id> [prefix]", "Create Tag."));
        sender.sendMessage(help(" /dogtags remove <id>", "Remove Tag."));
        sender.sendMessage(help(" /dogtags perm <id>", "Tag Permission."));
        sender.sendMessage(" ");
    }
}
