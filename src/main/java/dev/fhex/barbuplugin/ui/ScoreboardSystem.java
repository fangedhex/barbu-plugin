package dev.fhex.barbuplugin.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import dev.fhex.barbuplugin.ui.boards.DeathBoard;
import dev.fhex.barbuplugin.ui.boards.RestBoard;

public class ScoreboardSystem implements Listener, Runnable {
    private Plugin plugin;
    private Scoreboard scoreboard;
    private Objective listObjective;
    private Drawer drawer;

    private DeathBoard deathBoard;
    private RestBoard restBoard;

    public ScoreboardSystem(Plugin plugin) {
        this.plugin = plugin;
        drawer = new Drawer();

        // Creating the scoreboard
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        // Creating a simple health show in player list
        listObjective = scoreboard.registerNewObjective("player_list", "dummy", "player_list");
        listObjective.setRenderType(RenderType.HEARTS);
        listObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        // Set our scoreboard to all players on the server
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }

        // Creating boards
        deathBoard = new DeathBoard();
        restBoard = new RestBoard();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 1, 20 * 5);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setScoreboard(scoreboard);
    }

    @Override
    public void run() {
        try {
            for (Player p : Bukkit.getOnlinePlayers()) {
                listObjective.getScore(p.getDisplayName()).setScore((int) p.getHealth());
            }

            // Clean up old objective
            Objective old = scoreboard.getObjective("sidebar");
            if (old != null) {
                old.unregister();
            }

            // Setup new objective
            Objective sidebar = scoreboard.registerNewObjective("sidebar", "dummy",
                    ChatColor.BOLD + plugin.getConfig().getString("sidebar_title"));
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

            deathBoard.update(drawer);
            drawer.separator();
            restBoard.update(drawer);

            drawer.flush(sidebar);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
