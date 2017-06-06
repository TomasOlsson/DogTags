package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.contexts.OnlinePlayer;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageHandler;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.dev.DogTag;
import io.chazza.dogtags.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class SetCommand extends BaseCommand implements Listener {

    @Subcommand("set") @Syntax("<player> <tag>")
    public void onCommand(CommandSender sender, OnlinePlayer p, String ID){
        if(!sender.hasPermission("dogtags.set")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

        if(DogTags.getTag(ID) != null){
            DogTag dt = DogTags.getTag(ID);
            StorageHandler.setUser(p.getPlayer(), dt.getId());
            sender.sendMessage(TagLang.SET_OTHER.get().replace("%id%", ID)
                .replace("%prefix%", dt.getPrefix()).replace("%player%", p.getPlayer().getName()));
            return;
        }else {
            sender.sendMessage(TagLang.INVALID_TAG.get());
            return;
        }
    }

    @Subcommand("setprefix") @Syntax("<tag> <prefix>")
    public void onCommand(CommandSender sender, String ID, String prefix){
        if(!sender.hasPermission("dogtags.set")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

        prefix = ColorUtil.translate(prefix);

        if(DogTags.getTag(ID) != null){
            DogTag dt = DogTags.getTag(ID);

            dt.setPrefix(prefix);
            DogTags.getInstance().getConfig().set("dogtags."+dt.getId()+".prefix", prefix);
            DogTags.getInstance().saveConfig();
            DogTags.getInstance().reloadConfig();

            DogTags.getConnection().setTagPrefix(dt.getId(), prefix);

            sender.sendMessage(TagLang.SET_TAG_PREFIX.get().replace("%id%", ID)
                .replace("%prefix%", prefix));
            return;
        }else {
            sender.sendMessage(TagLang.INVALID_TAG.get());
            return;
        }
    }

    @Subcommand("setdesc") @Syntax("<tag> <description>")
    public void onDescCommand(CommandSender sender, String ID, String description){
        if(!sender.hasPermission("dogtags.set")) {sender.sendMessage(TagLang.NO_PERMISSION.get()); return; }

        description = ColorUtil.translate(description);

        if(DogTags.getTag(ID) != null){
            DogTag dt = DogTags.getTag(ID);

            dt.setDescription(description);
            DogTags.getInstance().getConfig().set("dogtags."+dt.getId()+".description", description);
            DogTags.getInstance().saveConfig();
            DogTags.getInstance().reloadConfig();

            DogTags.getConnection().setTagDesc(dt.getId(), description);

            sender.sendMessage(TagLang.SET_TAG_DESCRIPTION.get().replace("%id%", ID)
                .replace("%description%", description));
            return;
        }else {
            sender.sendMessage(TagLang.INVALID_TAG.get());
            return;
        }
    }

}
