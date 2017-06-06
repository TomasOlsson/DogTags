package io.chazza.dogtags.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {
    
    private final UUID u;
    private FileConfiguration fc;
    private File file;
    private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(this.getClass());
    private static List<UserManager> configs = new ArrayList<>();

    private UserManager(Player p) {
        this.u = p.getUniqueId();

        configs.add(this);
    }
    
    private UserManager(UUID u) {
        this.u = u;

        configs.add(this);  
    }

    public Player getOwner() {
        if (u == null)
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return Bukkit.getPlayer(u);
    }
    
    public UUID getOwnerUUID() {
        if (u == null)
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return u;
    }
    
    public JavaPlugin getInstance() {
        if (plugin == null)
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return plugin;
    }

    public static UserManager getConfig(Player p) {
        for (UserManager c : configs) {
               if (c.getOwnerUUID().equals(p.getUniqueId())) {
                   return c;
                }
        }
        return new UserManager(p);
    }
    
    public static UserManager getConfig(UUID u) {
        for (UserManager c : configs) {
               if (c.getOwnerUUID().equals(u)) {
                   return c;
                }
        }
        return new UserManager(u);
    }

    public boolean delete() {
        return getFile().delete();
    }

    public boolean exists() {
        if (fc == null || file == null) {
            File temp = new File(getDataFolder() + File.separator + "user-data", getOwnerUUID() + ".yml");
            if (!temp.exists()) {
                return false;
            } else {
                file = temp;
            }
        }
        return true;
    }
    
    public File getDataFolder() {
        File dir = new File(UserManager.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File d = new File(dir.getParentFile().getPath(), getInstance().getName()  + File.separator + "user-data");
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    public File getFile() {
        if (file == null) {
            file = new File(getDataFolder(), getOwnerUUID() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public FileConfiguration getConfig() {
        if (fc == null) {
            fc = YamlConfiguration.loadConfiguration(getFile());
        }
        return fc;
    }

    public void reload() {
        if (file == null) {
            file = new File(getDataFolder() + File.separator + "user-data", getOwner().getUniqueId().toString() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fc = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void resetConfig() {
        delete();
        getConfig();
    }

    public void saveConfig() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}