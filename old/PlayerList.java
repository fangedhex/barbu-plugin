package dev.fhex.barbuplugin.ui;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;

public class PlayerList {
    public void applyToPlayer(Player player) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field a = packet.getClass().getDeclaredField("header");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("footer");
            b.setAccessible(true);

            ChatComponentText header = new ChatComponentText(ChatColor.UNDERLINE + "Barbu Land");

            World w = Bukkit.getWorlds().get(0);
            ChatComponentText footer = new ChatComponentText(w.getName() + " : " + w.getSeed());

            a.set(packet, header);
            b.set(packet, footer);

            if (Bukkit.getOnlinePlayers().size() == 0)
                return;
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
