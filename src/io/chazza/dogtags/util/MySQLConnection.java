package io.chazza.dogtags.util;

import io.chazza.dogtags.DogTags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by charliej on 08/05/2017.
 */
public class MySQLConnection {

    private Statement statement;
    private String host, database, username, password;
    private int port;

    private String prefix = DogTags.getInstance().getConfig().getString("storage.prefix");
    private String tagData = prefix + "tagdata";
    private String playerData = prefix + "playerdata";


    public MySQLConnection(String host, String database, String username, String password, int port){
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    Connection conn = null;
    public void testConnection() throws SQLException, ClassNotFoundException {
        String databaseURL = "jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password="+password + "&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
        try {
            conn = DriverManager.getConnection(databaseURL);
            if (conn != null) {
                statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tagData + " ( `id` TEXT NULL DEFAULT NULL , `prefix` TEXT NULL DEFAULT NULL , `description` TEXT NULL DEFAULT NULL , `permission` BOOLEAN NOT NULL DEFAULT TRUE , UNIQUE `id` (`id`(15))) ENGINE = InnoDB;");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + playerData + " ( `uuid` TEXT NULL DEFAULT NULL , `nick` TEXT NULL DEFAULT NULL , `tag` TEXT NULL DEFAULT NULL , UNIQUE `uuid` (`uuid`(20))) ENGINE = InnoDB;");
                LogUtil.outputMsg("Successfully Connected.");
            }else{
                LogUtil.outputMsg("Could not connect.");
            }
        } catch (SQLException ex) {
            LogUtil.outputMsg("SQL Error! " + ex.getLocalizedMessage());
        }
    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public void insertTag(String tag, String prefix, String description, boolean permission){
        try {

            PreparedStatement prep = conn.prepareStatement("INSERT INTO " + tagData + " (id, prefix, description, permission) VALUES (?, ?, ?, ?);");
            prep.setString(1, tag);
            prep.setString(2, prefix);
            prep.setString(3, description);
            prep.setBoolean(4, permission);
            prep.execute();

        } catch(Exception e){
            Bukkit.getLogger().info("[DogTags] Failed to add the "+ tag + " tag.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public void removeTag(String tag){
        try {

            PreparedStatement prep = conn.prepareStatement("DELETE FROM " + tagData + " WHERE id = ?;");
            prep.setString(1, tag);
            prep.execute();

        } catch(SQLException e){
            Bukkit.getLogger().info("[DogTags] Failed to remove the "+ tag + " tag.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public void removeUserTag(UUID player){
        try {

            PreparedStatement prep = conn.prepareStatement("DELETE FROM " + playerData + " WHERE uuid = ?;");
            prep.setString(1, player.toString());
            prep.execute();

        } catch(SQLException e){
            Bukkit.getLogger().info("[DogTags] Failed to remove the "+ player + " tag.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }


    public String getTagPrefix(String id){
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT id,prefix FROM " + tagData + " WHERE id = '"+id+"';");

            while (result.next()) {
                return result.getString("prefix");
            }
        } catch (SQLException e) {
            LogUtil.outputMsg("Failed to get tag " + id + "'s prefix.");
            LogUtil.outputMsg("Error: " + e.getLocalizedMessage());
        }
        return id;
    }

    
    public String getTagCheck(String id){
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT id FROM " + tagData + " WHERE id = '"+id+"';");

            while (result.next()) {
                return result.getString("id");
            }
        } catch (SQLException e) {
            LogUtil.outputMsg("Failed to find tag " + id + ".");
            LogUtil.outputMsg("Error: " + e.getLocalizedMessage());
        }
        return id;
    }
    
    
    public List<String> getTags(){
        List<String> tags = new ArrayList<>();
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT id FROM " + tagData + ";");
            while (result.next()) {
                tags.add(result.getString("id"));
            }
        } catch (SQLException e) {
            LogUtil.outputMsg("SQL Error! " + e.getLocalizedMessage());
        }
        return tags;
    }


    public String getTagDesc(String id){
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT id,description FROM " + tagData + " WHERE id = '" + id + "';");
            while (result.next()) {
                return result.getString("description");
            }
        } catch (SQLException e) {
            LogUtil.outputMsg("Failed to get tag " + id + "'s prefix.");
            LogUtil.outputMsg("Error: " + e.getLocalizedMessage());
        }
        return "Â§7Default DogTags Description";
    }


    public boolean getTagPerm(String id){
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT id,permission FROM " + tagData + " WHERE id = '" + id + "';");
            while (result.next()) {
                return result.getBoolean("permission");
            }
        } catch (SQLException e) {
            LogUtil.outputMsg("Failed to get tag " + id + "'s permission.");
            LogUtil.outputMsg("Error: " + e.getLocalizedMessage());
        }
        return false;
    }


    /**
     * Works perfectly
     * @param p
     */

    public void setUserTag(Player p, String tag){
        UUID id = p.getUniqueId();
        String name = p.getName();

        try {
            PreparedStatement prep = conn.prepareStatement("INSERT INTO " + playerData + " (uuid, nick, tag) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE tag ='"+tag+"', nick = '" + name + "';");
            prep.setString(1, id.toString());
            prep.setString(2, name);
            prep.setString(3, tag);
            prep.execute();

        } catch(SQLException e){
            Bukkit.getLogger().info("[DogTags] Failed to add "+ name + " to database.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public void setTagPrefix(String tag, String prefix){
        try {
            PreparedStatement prep = conn.prepareStatement("UPDATE " + tagData + " SET prefix = ? WHERE id = ?;");
            prep.setString(1, prefix);
            prep.setString(2, tag);
            prep.execute();

        } catch(SQLException e){
            Bukkit.getLogger().info("[DogTags] Failed to add "+ tag + " to database.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public void setTagDesc(String tag, String desc){
        try {
            PreparedStatement prep = conn.prepareStatement("UPDATE " + tagData + " SET description = ? WHERE id = ?;");
            prep.setString(1, desc);
            prep.setString(2, tag);
            prep.execute();

        } catch(SQLException e){
            Bukkit.getLogger().info("[DogTags] Failed to add "+ tag + " to database.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public void setTagPerm(String tag, boolean permission){
        try {
            PreparedStatement prep = conn.prepareStatement("UPDATE " + tagData + " SET permission = ? WHERE id = ?;");
            prep.setBoolean(1, permission);
            prep.setString(2, tag);
            prep.execute();

        } catch(SQLException e){
            Bukkit.getLogger().info("[DogTags] Failed to set perms to "+ tag + " in database.");
            Bukkit.getLogger().info("[DogTags] Error '"+e.getLocalizedMessage()+"'.");
        }
    }

    public String getTag(UUID id){
        try {
            ResultSet result = conn.createStatement().executeQuery("SELECT tag FROM " + playerData + " WHERE uuid = '" + id + "';");
            while (result.next()) {
                return result.getString("tag");
            }
        } catch (SQLException e) {
            LogUtil.outputMsg("Failed to get user " + id + "'s tag.");
            LogUtil.outputMsg("Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
