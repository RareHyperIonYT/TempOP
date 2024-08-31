package me.RareHyperIon.TempOP.listeners;

import me.RareHyperIon.TempOP.TempOP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final TempOP plugin;

    public PlayerListener(final TempOP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandProcess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = this.getCommand(event.getMessage());

        if(!this.plugin.operators.containsKey(player.getUniqueId())) return;

        final FileConfiguration config = this.plugin.getConfig();

        if(command.equals("op") || command.equals("tempop")) {
            player.sendMessage(config.getString("message.op").replace('&', '§'));
            event.setCancelled(true);
        } else if(command.equals("deop")) {
            player.sendMessage(config.getString("message.deop").replace('&', '§'));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(this.plugin.isExpired(player.getUniqueId(), System.currentTimeMillis())) {
            if(!player.isOp()) {
                this.plugin.operators.remove(player.getUniqueId());
                return;
            }

            this.plugin.deop(player);
            player.sendMessage(this.plugin.getConfig().getString("message.expire").replace('&', '§'));
        }
    }

    private String getCommand(final String message) {
        String command = message.toLowerCase().split(" ")[0];
        if(command.contains(":")) command = command.split(":")[1];

        return command.trim().substring(1);
    }

}
