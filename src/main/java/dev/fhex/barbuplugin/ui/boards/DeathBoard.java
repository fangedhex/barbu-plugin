package dev.fhex.barbuplugin.ui.boards;

import dev.fhex.barbuplugin.App;
import dev.fhex.barbuplugin.ui.Board;
import dev.fhex.barbuplugin.ui.Drawer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.Comparator;

public class DeathBoard extends Board implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        redraw();
    }

    @Override
    public void update(Drawer drawer) {
        drawer.drawText("" + ChatColor.DARK_RED + ChatColor.BOLD + App.instance.getConfig().getString("death_label"));
        ArrayList<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        list.sort(new DeathComparator());

        for (int i = 0; i < 5 && i < list.size(); i++) {
            Player p = list.get(i);
            int death = p.getStatistic(Statistic.DEATHS);
            drawer.drawText((i + 1) + ": " + p.getDisplayName() + " (" + death + ")");
        }
    }

    private static class DeathComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return p1.getStatistic(Statistic.DEATHS) > p2.getStatistic(Statistic.DEATHS) ? -1 : 0;
        }

    }

}
