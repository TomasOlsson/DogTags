package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.TagLang;
import org.bukkit.command.CommandSender;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class ReloadCommand extends BaseCommand {

    @Subcommand("reload|rl")
    public void onCommand(CommandSender sender){
        if (!sender.hasPermission("dogtags.reload")) {
            sender.sendMessage(TagLang.NO_PERMISSION.get());
            return;
        }
        DogTags.getInstance().handleReload();

        sender.sendMessage(TagLang.RELOAD.get());
    }

}
