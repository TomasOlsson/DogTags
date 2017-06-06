package io.chazza.dogtags.cmd;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.TagLang;
import io.chazza.dogtags.dev.DogTag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by charliej on 01/06/2017.
 */
@CommandAlias("%command%")
public class PermCommand extends BaseCommand implements Listener {

    @Subcommand("perm|permission") @Syntax("<tag>")
    public void onCommand(Player p, String ID){
       if(DogTags.getTag(ID) != null){
           DogTag dt = DogTags.getTag(ID);

           if(dt.hasPermission()) p.sendMessage(TagLang.TAG_PERMISSION.get()
               .replace("%id%", dt.getId())
               .replace("%permission%", "dogtags.use."+dt.getId().toLowerCase()));

           else p.sendMessage(TagLang.TAG_NO_PERMISSION.get().replace("%id%", dt.getId()));
           return;
       }else {
           p.sendMessage(TagLang.INVALID_TAG.get());
           return;
       }
    }

}
