package dev.fhex.barbuplugin.ui;

import dev.fhex.barbuplugin.ui.boards.DeathBoard;
import dev.fhex.barbuplugin.ui.boards.RestBoard;
import dev.fhex.barbuplugin.ui.event.BoardRedrawEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class ScoreboardSystem implements Listener {
    private Plugin plugin;
    private Scoreboard scoreboard;
    private Drawer drawer;

    private DeathBoard deathBoard;
    private RestBoard restBoard;

    public ScoreboardSystem(Plugin plugin) {
        this.plugin = plugin;
        drawer = new Drawer();

        // Creating the scoreboard
        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

        // Creating a simple health show in player list
        Objective health = scoreboard.registerNewObjective("health", Criterias.HEALTH, "health");
        health.setRenderType(RenderType.HEARTS);
        health.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        // Set our scoreboard to all players on the server
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }

        // Creating boards
        deathBoard = new DeathBoard();
        plugin.getServer().getPluginManager().registerEvents(deathBoard, plugin);

        restBoard = new RestBoard();
        plugin.getServer().getPluginManager().registerEvents(restBoard, plugin);

        onBoardRedraw(null);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setScoreboard(scoreboard);
    }

    @EventHandler
    public void onBoardRedraw(BoardRedrawEvent ev) {
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
