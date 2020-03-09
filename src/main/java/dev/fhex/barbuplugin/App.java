package dev.fhex.barbuplugin;

import org.bukkit.plugin.java.JavaPlugin;

import dev.fhex.barbuplugin.ui.ScoreboardSystem;

public class App extends JavaPlugin {
    public static App instance;

    private BetterMotd motd;
    private ScoreboardSystem scoreboardSystem;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        motd = new BetterMotd();
        scoreboardSystem = new ScoreboardSystem(this);

        // Register event handler
        getServer().getPluginManager().registerEvents(motd, this);
        getServer().getPluginManager().registerEvents(scoreboardSystem, this);
    }
}
