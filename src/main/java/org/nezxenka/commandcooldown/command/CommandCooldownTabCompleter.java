package org.nezxenka.commandcooldown.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.nezxenka.commandcooldown.core.config.ConfigManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommandCooldownTabCompleter implements TabCompleter {

    private final ConfigManager configManager;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("commandcooldown.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return Arrays.asList("set", "remove", "clear").stream()
                    .filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("set") 
                || args[0].equalsIgnoreCase("remove") 
                || args[0].equalsIgnoreCase("clear"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(OfflinePlayer::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && (args[0].equalsIgnoreCase("set") 
                || args[0].equalsIgnoreCase("remove"))) {
            List<String> commands = new ArrayList<>();
            var cooldownSection = configManager.getCooldownConfig()
                    .getConfigurationSection("cooldowns");
            
            if (cooldownSection != null) {
                commands.addAll(cooldownSection.getKeys(false));
            }

            return commands.stream()
                    .filter(cmd -> cmd.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("set")) {
            return Arrays.asList("10", "30", "60", "300", "600", "3600").stream()
                    .filter(num -> num.startsWith(args[3]))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
