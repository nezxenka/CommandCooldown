package org.nezxenka.commandcooldown.core.config;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nezxenka.commandcooldown.CommandCooldown;

import java.io.File;
import java.io.IOException;

@Getter
public class MessageManager {

    private final CommandCooldown plugin;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public MessageManager(CommandCooldown plugin) {
        this.plugin = plugin;
        setupMessages();
    }

    private void setupMessages() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reloadMessages() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void saveMessages() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить messages.yml: " + e.getMessage());
        }
    }

    public String getMessage(String path) {
        String message = messagesConfig.getString(path, path);
        return colorize(message);
    }

    public String getMessage(String path, String... replacements) {
        String message = getMessage(path);
        
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        
        return message;
    }

    public String getPrefix() {
        return getMessage("prefix");
    }

    private String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String formatTime(long seconds) {
        if (seconds <= 0L) {
            return getMessage("time.zero");
        }

        long days = seconds / 86400L;
        seconds %= 86400L;
        long hours = seconds / 3600L;
        seconds %= 3600L;
        long minutes = seconds / 60L;
        seconds %= 60L;

        StringBuilder builder = new StringBuilder();

        if (days > 0L) {
            builder.append(days).append(getMessage("time.format.days"));
        }

        if (hours > 0L) {
            builder.append(hours).append(getMessage("time.format.hours"));
        }

        if (minutes > 0L) {
            builder.append(minutes).append(getMessage("time.format.minutes"));
        }

        if (seconds > 0L || builder.length() == 0) {
            builder.append(seconds).append(getMessage("time.format.seconds"));
        }

        return builder.toString().trim();
    }
}
