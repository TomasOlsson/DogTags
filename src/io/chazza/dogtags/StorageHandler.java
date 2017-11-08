ppackage io.chazza.dogtags;

import io.chazza.dogtags.DogTags;
import io.chazza.dogtags.StorageEnum;
import io.chazza.dogtags.dev.DogTag;
import io.chazza.dogtags.manager.ConfigManager;
import io.chazza.dogtags.manager.UserManager;
import io.chazza.dogtags.util.ColorUtil;
import io.chazza.dogtags.util.LogUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by charliej on 30/05/2017.
 */
public class StorageHandler {

    static FileConfiguration cnf = ConfigManager.get();

    public static void registerTags() {
        cnf = ConfigManager.get();

        DogTags.tags = new ArrayList<>();

        String desc, prefix;
        boolean permission;

        String item;
        Integer damage;

        if (DogTags.getStorage() == StorageEnum.FLATFILE) {
            for (String tag : cnf.getConfigurationSection("dogtags").getKeys(false)) {
                desc = !cnf.getString("dogtags." + tag + ".description").isEmpty()
                    ? ColorUtil.translate(cnf.getString("dogtags." + tag + ".description")) : "";
                prefix = ColorUtil.translate(cnf.getString("dogtags." + tag + ".prefix"));
                permission = cnf.getBoolean("dogtags." + tag + ".permission");

                item = cnf.getString("dogtags."+tag+".item") != null ? cnf.getString("dogtags."+tag+".item") : "NAME_TAG";
                damage = cnf.getInt("dogtags."+tag+".damage");

                ItemStack is = new ItemStack(Material.valueOf(item));
                is.setDurability(Short.valueOf(damage.toString()));

                DogTag dogTag = new DogTag(tag);
                dogTag.withItem(is).withDescription(desc).withPrefix(prefix).withPermission(permission).build(DogTags.getInstance());
                LogUtil.outputMsg("Registered " + tag + " tag!");
            }
        }else{
            for(String tag : DogTags.getConnection().getTags()){
                desc = ColorUtil.translate(DogTags.getConnection().getTagDesc(tag));
                prefix = ColorUtil.translate(DogTags.getConnection().getTagPrefix(tag));
                permission = DogTags.getConnection().getTagPerm(tag);

                DogTag dogTag = new DogTag(tag);
                dogTag.withDescription(desc).withPrefix(prefix).withPermission(permission).build(DogTags.getInstance());
                LogUtil.outputMsg("Registered " + tag + " tag!");
            }
        }
    }

    public static void addTag(String tag, String prefix, String description, boolean permission){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

            cnf.set("dogtags."+tag+".prefix", prefix);
            cnf.set("dogtags."+tag+".description", description);
            cnf.set("dogtags."+tag+".permission", true);
            

            DogTags.getInstance().handleReload();

        }else{
            DogTags.getConnection().insertTag(tag, prefix, description, permission);
        }
    }

    public static void removeTag(String tag){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

            cnf.set("dogtags."+tag, null);

            DogTags.getInstance().handleReload();

        }else{
            DogTags.getConnection().removeTag(tag);
        }
    }


    public static void setUser(Player p, String tag){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

            DogTag dt = DogTags.getTag(tag);

            UserManager um = UserManager.getConfig(p);
            um.getConfig().set("tag", dt.getId());
            um.saveConfig();
            um.reload();

        }else{
            DogTags.getConnection().setUserTag(p, tag);
        }
    }

    public static String getPlayerTag(Player p){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

            UserManager um = UserManager.getConfig(p);
            return um.getConfig().getString("tag");

        }else{
            return DogTags.getConnection().getTag(p.getUniqueId());
        }
    }
    
    public static void setPerm(String tag, boolean permission){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

        	cnf.set("dogtags."+tag+".permission", permission);

        }else{
            DogTags.getConnection().setTagPerm(tag, permission);
        }
    }

    public static void clearPlayerTag(Player p){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

            UserManager um = UserManager.getConfig(p);
            um.getConfig().set("tag", null);
            um.saveConfig();
            um.reload();

        }else{
            DogTags.getConnection().removeUserTag(p.getUniqueId());
        }
    }

    public static boolean getPerm(String tag){
        if (DogTags.getStorage() == StorageEnum.FLATFILE) {

        	return cnf.getBoolean("dogtags."+tag+".permission");

        }else{
        	return DogTags.getConnection().getTagPerm(tag);
        }
    }

}
