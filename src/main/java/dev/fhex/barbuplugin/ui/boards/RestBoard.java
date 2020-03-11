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
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class RestBoard extends Board implements Listener {

    private HashMap<Player, Integer> daysSinceSleep;

    public RestBoard() {
        daysSinceSleep = new HashMap<>();
    }

    @EventHandler
    public void onPlayerStatChange(PlayerStatisticIncrementEvent e) {
        if (e.getStatistic() == Statistic.TIME_SINCE_REST) {
            int days = e.getPlayer().getStatistic(Statistic.TIME_SINCE_REST) / 24000;
            if (!daysSinceSleep.containsKey(e.getPlayer())) {
                redraw();
            } else if (days != daysSinceSleep.get(e.getPlayer())) {
                redraw();
            }
        }
    }

    @Override
    public void update(Drawer drawer) {
        drawer.drawText(
                "" + ChatColor.WHITE + ChatColor.BOLD + App.instance.getConfig().getString("time_since_rest_label"));
        ArrayList<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        list.sort(new RestComparator());

        for (int i = 0; i < 5 && i < list.size(); i++) {
            Player p = list.get(i);
            int days = p.getStatistic(Statistic.TIME_SINCE_REST) / 24000;
            daysSinceSleep.put(p, days);
            drawer.drawText((i + 1) + ": " + p.getDisplayName() + " (" + days + ")");
        }
    }

    private static class RestComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return p1.getStatistic(Statistic.TIME_SINCE_REST) > p2.getStatistic(Statistic.TIME_SINCE_REST) ? -1 : 0;
        }
    }

}
