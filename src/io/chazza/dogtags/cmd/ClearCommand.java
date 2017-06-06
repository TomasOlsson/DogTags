package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.contexts.OnlinePlayer;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class ClearCommand extends BaseCommand implements Listener {

    @Subcommand("clear")
    public void onTagsCommand(CommandSender sender, @Optional OnlinePlayer target) {
        if (!sender.hasPermission("dogtags.clear")) {
            sender.sendMessage(TagLang.NO_PERMISSION.get());
            return;
        }

        if ((target != null) && (sender.hasPermission("dogtags.clear.other"))) {
            StorageHandler.clearPlayerTag(target.getPlayer());
            if(target.getPlayer() != sender) {
                sender.sendMessage(TagLang.CLEARED_OTHER.get().replace("%player%", target.getPlayer().getName()));
            }
            target.getPlayer().sendMessage(TagLang.CLEARED.get());
            return;
        } else if(sender instanceof Player) {
            Player p = (Player) sender;

            StorageHandler.clearPlayerTag(p);
            sender.sendMessage(TagLang.CLEARED.get());
            return;
        }
    }
}
