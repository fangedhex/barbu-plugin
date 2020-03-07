package dev.fhex.barbuplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class App extends JavaPlugin implements Listener {
    private Scoreboard scoreboard;
    private Objective timeSinceRestObjective;
    private Objective deathCountObjective;
    private Objective listObjective;
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
        listObjective = scoreboard.registerNewObjective("player_list", "dummy", "Liste des joueurs");

        listObjective.setRenderType(RenderType.HEARTS);
        listObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        timeSinceRestObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        listObjective.getScore("Garbage").setScore(0);

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

                    double health = p.getHealth();
                    listObjective.getScore(p.getDisplayName()).setScore((int) health);
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

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        World w = Bukkit.getWorlds().get(0);
        long ticks = w.getTime();
        long minute = ticks / 1200;
        long second = ticks / 20 - minute * 60;
        String s = "" + Math.round(minute) + ":" + Math.round(second);
        e.setMotd(ChatColor.BOLD + "Barbus Land" + ChatColor.RESET + " - " + s);
    }
}
