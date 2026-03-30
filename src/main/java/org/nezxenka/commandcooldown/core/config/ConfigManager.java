package org.nezxenka.commandcooldown.core.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nezxenka.commandcooldown.CommandCooldown;

import java.io.File;
import java.io.IOException;

@Getter
public class ConfigManager {

    private final CommandCooldown plugin;
    private FileConfiguration cooldownConfig;
    private File cooldownFile;

    public ConfigManager(CommandCooldown plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        cooldownFile = new File(plugin.getDataFolder(), "config.yml");
        if (!cooldownFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
    }

    public void reloadConfig() {
        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
    }

    public void saveConfig() {
        try {
            cooldownConfig.save(cooldownFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить config.yml: " + e.getMessage());
        }
    }
}
