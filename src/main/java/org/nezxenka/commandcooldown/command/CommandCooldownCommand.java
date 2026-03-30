package org.nezxenka.commandcooldown.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nezxenka.commandcooldown.core.CooldownManager;
import org.nezxenka.commandcooldown.core.config.MessageManager;
import org.nezxenka.commandcooldown.core.database.DatabaseManager;

import java.util.UUID;

@RequiredArgsConstructor
public class CommandCooldownCommand implements CommandExecutor {

    private final CooldownManager cooldownManager;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("commandcooldown.admin")) {
            sender.sendMessage(messageManager.getMessage("command.no-permission",
                "{prefix}", messageManager.getPrefix()));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(messageManager.getMessage("command.usage.main",
                "{prefix}", messageManager.getPrefix()));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        return switch (subCommand) {
            case "set" -> handleSet(sender, args);
            case "remove" -> handleRemove(sender, args);
            case "clear" -> handleClear(sender, args);
            default -> {
                sender.sendMessage(messageManager.getMessage("command.unknown-subcommand",
                    "{prefix}", messageManager.getPrefix()));
                yield true;
            }
        };
    }

    private boolean handleSet(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(messageManager.getMessage("command.usage.set",
                "{prefix}", messageManager.getPrefix()));
            return true;
        }

        String playerName = args[1];

        long cooldown;
        try {
            cooldown = Long.parseLong(args[args.length - 1]);
            if (cooldown < 0L) {
                sender.sendMessage(messageManager.getMessage("command.negative-number",
                    "{prefix}", messageManager.getPrefix()));
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(messageManager.getMessage("command.invalid-number",
                "{prefix}", messageManager.getPrefix()));
            return true;
        }

        StringBuilder commandBuilder = new StringBuilder();
        for (int i = 2; i < args.length - 1; i++) {
            if (commandBuilder.length() > 0) {
                commandBuilder.append(" ");
            }
            commandBuilder.append(args[i]);
        }

        String cmd = commandBuilder.toString();
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null) {
            sender.sendMessage(messageManager.getMessage("command.player-not-found",
                "{prefix}", messageManager.getPrefix(),
                "{player}", playerName));
            return true;
        }

        UUID playerId = targetPlayer.getUniqueId();
        cooldownManager.setCooldown(playerId, cmd, cooldown);
        
        sender.sendMessage(messageManager.getMessage("command.set-success",
            "{prefix}", messageManager.getPrefix(),
            "{command}", cmd,
            "{player}", playerName,
            "{time}", cooldown + " секунд"));

        return true;
    }

    private boolean handleRemove(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(messageManager.getMessage("command.usage.remove",
                "{prefix}", messageManager.getPrefix()));
            return true;
        }

        String playerName = args[1];
        String cmd = args[2];
        
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null) {
            sender.sendMessage(messageManager.getMessage("command.player-not-found",
                "{prefix}", messageManager.getPrefix(),
                "{player}", playerName));
            return true;
        }

        UUID playerId = targetPlayer.getUniqueId();
        databaseManager.removeCooldown(playerId, cmd);
        
        sender.sendMessage(messageManager.getMessage("command.remove-success",
            "{prefix}", messageManager.getPrefix(),
            "{command}", cmd,
            "{player}", playerName));

        return true;
    }

    private boolean handleClear(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(messageManager.getMessage("command.usage.clear",
                "{prefix}", messageManager.getPrefix()));
            return true;
        }

        String playerName = args[1];
        Player targetPlayer = Bukkit.getPlayer(playerName);
        
        if (targetPlayer == null) {
            sender.sendMessage(messageManager.getMessage("command.player-not-found",
                "{prefix}", messageManager.getPrefix(),
                "{player}", playerName));
            return true;
        }

        UUID playerId = targetPlayer.getUniqueId();
        databaseManager.clearPlayerCooldowns(playerId);
        
        sender.sendMessage(messageManager.getMessage("command.clear-success",
            "{prefix}", messageManager.getPrefix(),
            "{player}", playerName));

        return true;
    }
}
