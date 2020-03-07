package dev.fhex.barbuplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class App extends JavaPlugin implements Listener {
    private Scoreboard scoreboard;
    private Objective timeSinceRestObjective;
    private Objective deathCountObjective;
    private boolean state = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info("Preparing Barbu plugin");

        // Register event handler
        getServer().getPluginManager().registerEvents(this, this);

        // Creating scoreboard
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        timeSinceRestObjective = scoreboard.registerNewObjective("time_since_rest", "dummy",
                ChatColor.RED + getConfig().getString("time_since_rest_label"));
        deathCountObjective = scoreboard.registerNewObjective("death", "dummy",
                ChatColor.RED + getConfig().getString("death_label"));

        timeSinceRestObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set our scoreboard to all players on the server
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    int value = p.getStatistic(Statistic.TIME_SINCE_REST);
                    timeSinceRestObjective.getScore(p.getDisplayName()).setScore(value / 24000);
                    int value2 = p.getStatistic(Statistic.DEATHS);
                    deathCountObjective.getScore(p.getDisplayName()).setScore(value2);
                }
            }
        }, 0, 20);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                state = !state;
                if (state) {
                    timeSinceRestObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                } else {
                    deathCountObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                }
            }
        }, 0, 20 * 10);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setScoreboard(scoreboard);
    }
}
