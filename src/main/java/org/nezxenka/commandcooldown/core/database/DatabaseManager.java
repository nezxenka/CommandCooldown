package org.nezxenka.commandcooldown.core.database;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nezxenka.commandcooldown.CommandCooldown;
import org.nezxenka.commandcooldown.core.database.impl.MySQLDatabase;
import org.nezxenka.commandcooldown.core.database.impl.SQLiteDatabase;
import org.nezxenka.commandcooldown.model.DatabaseType;

import java.io.File;
import java.util.UUID;

@Getter
public class DatabaseManager {

    private final CommandCooldown plugin;
    private IDatabase database;
    private DatabaseType databaseType;

    public DatabaseManager(CommandCooldown plugin) {
        this.plugin = plugin;
        loadDatabaseConfig();
        initializeDatabase();
    }

    private void loadDatabaseConfig() {
        File dbFile = new File(plugin.getDataFolder(), "database.yml");
        if (!dbFile.exists()) {
            plugin.saveResource("database.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(dbFile);
        String typeStr = config.getString("storage.type", "SQLITE").toUpperCase();
        
        try {
            this.databaseType = DatabaseType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Неизвестный тип БД: " + typeStr + ". Используется SQLite.");
            this.databaseType = DatabaseType.SQLITE;
        }
    }

    private void initializeDatabase() {
        switch (databaseType) {
            case MYSQL -> database = new MySQLDatabase(plugin);
            case SQLITE -> database = new SQLiteDatabase(plugin);
            default -> {
                plugin.getLogger().severe("Неподдерживаемый тип БД!");
                database = new SQLiteDatabase(plugin);
            }
        }

        database.connect();
        database.createTable();
    }

    public Long getCooldown(UUID playerId, String command) {
        return database.getCooldown(playerId, command);
    }

    public void setCooldown(UUID playerId, String command, long cooldownEnd) {
        database.setCooldown(playerId, command, cooldownEnd);
    }

    public void removeCooldown(UUID playerId, String command) {
        database.removeCooldown(playerId, command);
    }

    public void clearPlayerCooldowns(UUID playerId) {
        database.clearPlayerCooldowns(playerId);
    }

    public void closeConnection() {
        if (database != null) {
            database.closeConnection();
        }
    }
}
