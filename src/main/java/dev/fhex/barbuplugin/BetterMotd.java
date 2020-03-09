package dev.fhex.barbuplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class BetterMotd implements Listener {
    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        String oldMotd = e.getMotd();
        World w = Bukkit.getWorlds().get(0);
        long ticks = w.getTime();
        long hours = ticks / 1000;
        long minutes = (long) ((ticks - hours * 1000) / 16.6);
        String clock = String.format("%02d:%02d", hours, minutes);
        e.setMotd(ChatColor.BOLD + oldMotd + ChatColor.RESET + " - " + clock);
    }
}
