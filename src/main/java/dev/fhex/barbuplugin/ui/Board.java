package dev.fhex.barbuplugin.ui;

import dev.fhex.barbuplugin.ui.event.BoardRedrawEvent;
import org.bukkit.Bukkit;

public class Board {
    public void redraw() {
        BoardRedrawEvent event = new BoardRedrawEvent();
        Bukkit.getPluginManager().callEvent(event);
    }

    public void update(Drawer drawer) {
    }
}
