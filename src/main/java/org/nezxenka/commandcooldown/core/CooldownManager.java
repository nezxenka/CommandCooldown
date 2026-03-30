package org.nezxenka.commandcooldown.core;

import lombok.RequiredArgsConstructor;
import org.nezxenka.commandcooldown.CommandCooldown;
import org.nezxenka.commandcooldown.core.config.ConfigManager;
import org.nezxenka.commandcooldown.core.database.DatabaseManager;

import java.util.UUID;

@RequiredArgsConstructor
public class CooldownManager {

    private final CommandCooldown plugin;
    private final DatabaseManager databaseManager;
    private final ConfigManager configManager;

    public boolean isOnCooldown(UUID playerId, String command) {
        long currentTime = System.currentTimeMillis();
        Long cooldownEnd = databaseManager.getCooldown(playerId, command);
        return cooldownEnd != null && currentTime < cooldownEnd;
    }

    public void setCooldown(UUID playerId, String command, long seconds) {
        long cooldownEnd = System.currentTimeMillis() + seconds * 1000L;
        databaseManager.setCooldown(playerId, command, cooldownEnd);
    }

    public long getRemainingCooldown(UUID playerId, String command) {
        Long cooldownEnd = databaseManager.getCooldown(playerId, command);
        if (cooldownEnd == null) {
            return 0L;
        }
        long remaining = (cooldownEnd - System.currentTimeMillis()) / 1000L;
        return Math.max(remaining, 0L);
    }

    public void removeCooldown(UUID playerId, String command) {
        databaseManager.removeCooldown(playerId, command);
    }

    public void clearPlayerCooldowns(UUID playerId) {
        databaseManager.clearPlayerCooldowns(playerId);
    }

    public boolean isCommandTracked(String command) {
        var cooldownSection = configManager.getCooldownConfig().getConfigurationSection("cooldowns");
        if (cooldownSection == null) {
            return false;
        }

        String commandLower = command.toLowerCase();
        return cooldownSection.getKeys(false).stream()
                .anyMatch(cmd -> {
                    String cmdLower = cmd.toLowerCase();
                    return commandLower.equals(cmdLower) || commandLower.startsWith(cmdLower + " ");
                });
    }

    public long getCommandCooldown(String command) {
        var cooldownSection = configManager.getCooldownConfig().getConfigurationSection("cooldowns");
        if (cooldownSection == null) {
            return 0L;
        }

        String commandLower = command.toLowerCase();
        String bestMatch = null;
        int bestMatchLength = 0;

        for (String cmd : cooldownSection.getKeys(false)) {
            String cmdLower = cmd.toLowerCase();
            
            if (commandLower.equals(cmdLower)) {
                return configManager.getCooldownConfig().getLong("cooldowns." + cmd);
            }

            if (commandLower.startsWith(cmdLower + " ") && cmdLower.length() > bestMatchLength) {
                bestMatch = cmd;
                bestMatchLength = cmdLower.length();
            }
        }

        if (bestMatch != null) {
            return configManager.getCooldownConfig().getLong("cooldowns." + bestMatch);
        }

        return 0L;
    }
}
