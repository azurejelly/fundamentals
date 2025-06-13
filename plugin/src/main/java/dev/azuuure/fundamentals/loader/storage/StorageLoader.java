package dev.azuuure.fundamentals.loader.storage;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.loader.Loader;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.factory.StorageFactory;

import java.util.logging.Level;

public class StorageLoader implements Loader {

    private final Fundamentals plugin;

    public StorageLoader(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        try {
            StorageFactory factory = new StorageFactory(plugin, plugin.getConfig());
            plugin.setStorage(factory.create());
            plugin.getStorage().init();
        } catch (StorageInitializationException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize storage", ex);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}
