package dev.azuuure.fundamentals.loader.listener;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.Loader;
import dev.azuuure.fundamentals.listener.PlayerJoinListener;

public class ListenerLoader implements Loader {

    private final Fundamentals plugin;

    public ListenerLoader(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin), plugin);
    }
}
