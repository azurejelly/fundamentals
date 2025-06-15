package dev.azuuure.fundamentals.loader.listener;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.loader.Loader;
import dev.azuuure.fundamentals.listener.AsyncPlayerPreLoginListener;
import dev.azuuure.fundamentals.listener.PlayerJoinListener;
import dev.azuuure.fundamentals.listener.PlayerQuitListener;
import dev.triumphteam.cmd.core.annotation.Async;

public class ListenerLoader implements Loader {

    private final Fundamentals plugin;

    public ListenerLoader(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        plugin.getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(plugin), plugin);
    }
}
