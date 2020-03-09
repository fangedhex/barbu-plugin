package dev.fhex.barbuplugin.ui.boards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import dev.fhex.barbuplugin.App;
import dev.fhex.barbuplugin.ui.Board;
import dev.fhex.barbuplugin.ui.Drawer;
import net.md_5.bungee.api.ChatColor;

public class RestBoard implements Board {

    private class RestComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return p1.getStatistic(Statistic.TIME_SINCE_REST) > p2.getStatistic(Statistic.TIME_SINCE_REST) ? -1 : 0;
        }

    }

    @Override
    public void update(Drawer drawer) {
        drawer.drawText(
                "" + ChatColor.WHITE + ChatColor.BOLD + App.instance.getConfig().getString("time_since_rest_label"));
        ArrayList<Player> list = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        Collections.sort(list, new RestComparator());

        for (int i = 0; i < 5 && i < list.size(); i++) {
            Player p = list.get(i);
            int death = p.getStatistic(Statistic.TIME_SINCE_REST) / 24000;
            drawer.drawText((i + 1) + ": " + p.getDisplayName() + " (" + death + ")");
        }
    }

}
