package me.RareHyperIon.TempOP;

import me.RareHyperIon.TempOP.commands.TempCommand;
import me.RareHyperIon.TempOP.handler.DataHandler;
import me.RareHyperIon.TempOP.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TempOP extends JavaPlugin {

    public final Map<UUID, Long> operators = new HashMap<>();
    private DataHandler dataHandler;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.operators.putAll((this.dataHandler = new DataHandler(this.getLogger())).load(this.getDataFolder()));

        final TempCommand tempCommand = new TempCommand(this);
        this.getCommand("tempop").setExecutor(tempCommand);
        this.getCommand("tempop").setTabCompleter(tempCommand);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        Bukkit.getScheduler().runTaskTimer(this, this::checkOperators, 0, 20L);
    }

    @Override
    public void onDisable() {
        this.dataHandler.save(this.getDataFolder(), this.operators);
        this.operators.clear();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void op(final Player player, final long timestamp) {
        this.operators.put(player.getUniqueId(), timestamp);
        player.setOp(true);
    }

    public void deop(final Player player) {
        this.operators.remove(player.getUniqueId());
        player.setOp(false);
    }

    public boolean isExpired(final UUID uuid, final long currentTime) {
        return this.operators.containsKey(uuid) && this.operators.get(uuid) <= currentTime;
    }

    private void checkOperators() {
        final long currentTime = System.currentTimeMillis();

        for(Map.Entry<UUID, Long> entry : this.operators.entrySet()) {
            final Player player = Bukkit.getPlayer(entry.getKey());
            if(player == null) continue;  // Ignore, it will be handled when they join back.

            if(!player.isOp()) {
                this.operators.remove(entry.getKey());
                return;
            }

            if(!this.isExpired(entry.getKey(), currentTime)) continue;

            this.deop(player);
            player.sendMessage(this.getConfig().getString("message.expire").replace('&', '§'));
        }
    }

}

