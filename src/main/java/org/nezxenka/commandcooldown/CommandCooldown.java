package org.nezxenka.commandcooldown;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nezxenka.commandcooldown.api.CooldownAPI;
import org.nezxenka.commandcooldown.command.CommandCooldownCommand;
import org.nezxenka.commandcooldown.command.CommandCooldownTabCompleter;
import org.nezxenka.commandcooldown.core.CooldownManager;
import org.nezxenka.commandcooldown.core.config.ConfigManager;
import org.nezxenka.commandcooldown.core.config.MessageManager;
import org.nezxenka.commandcooldown.core.database.DatabaseManager;
import org.nezxenka.commandcooldown.listener.CommandListener;

@Getter
public final class CommandCooldown extends JavaPlugin {

    @Getter
    private static CommandCooldown instance;
    
    private ConfigManager configManager;
    private MessageManager messageManager;
    private DatabaseManager databaseManager;
    private CooldownManager cooldownManager;
    private CooldownAPI cooldownAPI;

    @Override
    public void onEnable() {
        instance = this;
        
        initializeManagers();
        registerCommands();
        registerListeners();
        
        getLogger().info("CommandCooldown v1.0 успешно загружен!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            try {
                databaseManager.closeConnection();
            } catch (Exception e) {
                getLogger().warning("Ошибка при закрытии соединения с БД: " + e.getMessage());
            }
        }
        
        getLogger().info("CommandCooldown выгружен!");
    }
    
    private void initializeManagers() {
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.cooldownManager = new CooldownManager(this, databaseManager, configManager);
        this.cooldownAPI = new CooldownAPI(cooldownManager);
    }
    
    private void registerCommands() {
        var command = Bukkit.getPluginCommand("commandcooldown");
        if (command != null) {
            command.setExecutor(new CommandCooldownCommand(cooldownManager, databaseManager, messageManager));
            command.setTabCompleter(new CommandCooldownTabCompleter(configManager));
        }
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
            new CommandListener(cooldownManager, messageManager), 
            this
        );
    }
}
