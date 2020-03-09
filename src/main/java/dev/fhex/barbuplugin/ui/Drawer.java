package dev.fhex.barbuplugin.ui;

import java.util.ArrayList;

import org.bukkit.scoreboard.Objective;

import net.md_5.bungee.api.ChatColor;

public class Drawer {
    private ArrayList<String> lines;

    public Drawer() {
        lines = new ArrayList<String>();
    }

    private String emptyChar() {
        return new String(new char[lines.size()]).replace("\0", "" + ChatColor.RESET);
    }

    public void drawText(String text) {
        lines.add(text + emptyChar());
    }

    public void separator() {
        drawText("");
    }

    public void flush(Objective objective) {
        for (int i = 0; i < lines.size(); i++) {
            objective.getScore(lines.get(i)).setScore(lines.size() - i);
        }

        lines.clear();
    }
}
