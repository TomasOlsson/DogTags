package io.chazza.dogtags;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by charliej on 13/05/2017.
 */
public enum TagLang {
    NO_PERMISSION("&6[&eDogTags&6] &7You do not have permission to do that."),

    // Admin Related //
    RELOAD("&6[&eDogTags&6] &7Reloaded Configuration!"),

    // Create | Remove Command //
    CREATED("&6[&eDogTags&6] &7Created &e%id% &7tag with prefix %prefix%&7."),
    REMOVED("&6[&eDogTags&6] &7Removed &e%id% &7tag."),

    // Set Command //
    SET("&6[&eDogTags&6] &7Tag &e%id% &7(%prefix%&7) selected."),
    SET_OTHER("&6[&eDogTags&6] &7You set %player%'s tag to &e%id% &7(%prefix%&7)."),
    SET_TAG_PREFIX("&6[&eDogTags&6] &7The prefix for &e%id% &7has been set to &f%prefix%&7."),
    SET_TAG_DESCRIPTION("&6[&eDogTags&6] &7The description for &e%id% &7has been set to &f%description%&7."),

    // Perm Command //
    TAG_NO_PERMISSION("&6[&eDogTags&6] &7The %id% tag requires no permission to use."),
    TAG_PERMISSION("&6[&eDogTags&6] &7The &e%id%&7 tag requires &6%permission% &7to use."),

    // Clear Command //
    CLEARED("&6[&eDogTags&6] &7Your tag has been cleared."),
    CLEARED_OTHER("&6[&eDogTags&6] &7You have cleared &e%player%'s &7tag."),

    ALREADY_EXISTS("&6[&eDogTags&6] &7That tag already exists."),
    INVALID_TAG("&6[&eDogTags&6] &7That is an invalid tag.");

    private String def;

    private FileConfiguration config;
    private YamlConfiguration yc;
    private DogTags plugin;

    TagLang(String def) {
        this.def = def;
        plugin = DogTags.getPlugin(DogTags.class);
        config = DogTags.getInstance().getConfig();
    }


    public String get() {
        yc = YamlConfiguration.loadConfiguration(new File(DogTags.getInstance().getDataFolder(), "config.yml"));

        String value = yc.getString("message."+name());
        if(value == null) return "§cMissing Message";
        return ChatColor.translateAlternateColorCodes('&', value);
    }
}
