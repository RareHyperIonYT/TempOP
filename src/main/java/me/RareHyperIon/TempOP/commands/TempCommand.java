package me.RareHyperIon.TempOP.commands;

import me.RareHyperIon.TempOP.TempOP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class TempCommand implements CommandExecutor, TabCompleter {

    private final TempOP plugin;

    public TempCommand(final TempOP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final FileConfiguration config = this.plugin.getConfig();

        if(!(sender instanceof Player) && !config.getBoolean("console-allowed")) {
            this.sendMessage(sender, config.getString("message.console"));
            return true;
        }

        if(!sender.hasPermission("tempop.use")) {
            this.sendMessage(sender, config.getString("message.permission"));
            return true;
        }

        if(sender instanceof Player && this.plugin.operators.containsKey(((Player) sender).getUniqueId())) {
            this.sendMessage(sender, config.getString("message.op"));
            return true;
        }

        if(args.length == 0) return false;

        final Player player = Bukkit.getPlayer(args[0]);

        if(player == null) {
            this.sendMessage(sender, String.format(config.getString("message.player"), args[0]));
            return true;
        }

        if(player.isOp()) {
            this.sendMessage(sender, config.getString("message.already-opped"));
            return true;
        }

        final String duration = args.length == 1 ? config.getString("default-duration") : this.concat(args);

        try {
            final long endTime = this.parseTime(duration);

            this.plugin.op(player, endTime);

            this.sendMessage(sender, config.getString("message.give")
                    .replaceAll("\\{player}", player.getName())
                    .replaceAll("\\{duration}", duration)
            );

            this.sendMessage(sender, config.getString("message.given")
                    .replaceAll("\\{player}", player.getName())
                    .replaceAll("\\{duration}", duration)
            );
        } catch (final IllegalArgumentException exception) {
            this.sendMessage(sender, exception.getMessage());
        }

        return true;
    }

    private void sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(message.replace('&', '§'));
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if(args.length == 1) return null;
        else if(args[1].isEmpty()) return Collections.singletonList("10s / 1d / 1w");
        return Collections.emptyList();
    }

    private String concat(final String[] args) {
        final StringJoiner joiner = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++) joiner.add(args[i]);
        return joiner.toString();
    }

    private long parseTime(final String time) {
        final String[] components = time.split("\\s+");
        long endTime = System.currentTimeMillis();

        for(final String component : components) {
            final String timeValue = component.replaceAll("[^0-9]", "");
            final String timeUnit = component.replaceAll("[0-9]", "");

            long multiplier;

            switch (timeUnit) {
                case "s": multiplier = 1000L; break;
                case "m": multiplier = 60 * 1000L; break;
                case "h": multiplier = 60 * 60 * 1000L; break;
                case "d": multiplier = 24 * 60 * 60 * 1000L; break;
                case "w": multiplier = 7 * 24 * 60 * 60 * 1000L; break;
                case "mo": multiplier = 30 * 24 * 60 * 60 * 1000L; break;
                default: throw new IllegalArgumentException("&cInvalid time format provided: " + component);
            }

            try {
                endTime += Integer.parseInt(timeValue) * multiplier;
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("&cInvalid time value: " + component);
            }
        }

        return endTime;
    }


}
