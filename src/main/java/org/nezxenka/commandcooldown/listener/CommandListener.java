package org.nezxenka.commandcooldown.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.nezxenka.commandcooldown.core.CooldownManager;
import org.nezxenka.commandcooldown.core.config.MessageManager;

@RequiredArgsConstructor
public class CommandListener implements Listener {

    private final CooldownManager cooldownManager;
    private final MessageManager messageManager;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        
        if (player.hasPermission("commandcooldown.bypass")) {
            return;
        }

        String fullCommand = event.getMessage().substring(1);

        if (!cooldownManager.isCommandTracked(fullCommand)) {
            return;
        }

        if (cooldownManager.isOnCooldown(player.getUniqueId(), fullCommand)) {
            long remaining = cooldownManager.getRemainingCooldown(player.getUniqueId(), fullCommand);
            String timeFormatted = messageManager.formatTime(remaining);
            
            event.setCancelled(true);
            player.sendMessage(messageManager.getMessage("cooldown.active", 
                "{prefix}", messageManager.getPrefix(),
                "{time}", timeFormatted));
        } else {
            long cooldown = cooldownManager.getCommandCooldown(fullCommand);
            if (cooldown > 0L) {
                cooldownManager.setCooldown(player.getUniqueId(), fullCommand, cooldown);
            }
        }
    }
}
