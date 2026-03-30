package org.nezxenka.commandcooldown.core.database.impl;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nezxenka.commandcooldown.CommandCooldown;
import org.nezxenka.commandcooldown.core.database.IDatabase;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class MySQLDatabase implements IDatabase {

    private final CommandCooldown plugin;
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public MySQLDatabase(CommandCooldown plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "database.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String host = config.getString("mysql.host", "localhost");
        int port = config.getInt("mysql.port", 3306);
        String database = config.getString("mysql.database", "commandcooldown");
        this.user = config.getString("mysql.username", "root");
        this.password = config.getString("mysql.password", "password");
        boolean useSSL = config.getBoolean("mysql.useSSL", false);

        this.url = String.format("jdbc:mysql://%s:%d/%s?useSSL=%b&autoReconnect=true", 
            host, port, database, useSSL);
    }

    @Override
    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            plugin.getLogger().info(plugin.getMessageManager()
                .getMessage("database.connected", "{type}", "MySQL"));
        } catch (Exception e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.connection-error", "{error}", e.getMessage()));
        }
    }

    @Override
    public void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                plugin.getLogger().warning(plugin.getMessageManager()
                    .getMessage("database.connection-lost"));
                connect();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.query-error", "{error}", e.getMessage()));
        }
    }

    @Override
    public void createTable() {
        checkConnection();
        String sql = """
            CREATE TABLE IF NOT EXISTS cmdcooldowns (
                player_uuid VARCHAR(36) NOT NULL,
                command VARCHAR(255) NOT NULL,
                cooldown_end BIGINT NOT NULL,
                PRIMARY KEY (player_uuid, command)
            )
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.table-create-error", "{error}", e.getMessage()));
        }
    }

    @Override
    public Long getCooldown(UUID playerId, String command) {
        checkConnection();
        String sql = "SELECT cooldown_end FROM cmdcooldowns WHERE player_uuid = ? AND command = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerId.toString());
            pstmt.setString(2, command);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("cooldown_end");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.query-error", "{error}", e.getMessage()));
        }
        
        return null;
    }

    @Override
    public void setCooldown(UUID playerId, String command, long cooldownEnd) {
        checkConnection();
        String sql = """
            INSERT INTO cmdcooldowns (player_uuid, command, cooldown_end) 
            VALUES (?, ?, ?) 
            ON DUPLICATE KEY UPDATE cooldown_end = VALUES(cooldown_end)
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerId.toString());
            pstmt.setString(2, command);
            pstmt.setLong(3, cooldownEnd);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.query-error", "{error}", e.getMessage()));
        }
    }

    @Override
    public void removeCooldown(UUID playerId, String command) {
        checkConnection();
        String sql = "DELETE FROM cmdcooldowns WHERE player_uuid = ? AND command = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerId.toString());
            pstmt.setString(2, command);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.query-error", "{error}", e.getMessage()));
        }
    }

    @Override
    public void clearPlayerCooldowns(UUID playerId) {
        checkConnection();
        String sql = "DELETE FROM cmdcooldowns WHERE player_uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerId.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.query-error", "{error}", e.getMessage()));
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info(plugin.getMessageManager()
                    .getMessage("database.connection-closed"));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(plugin.getMessageManager()
                .getMessage("database.close-error", "{error}", e.getMessage()));
        }
    }
}
