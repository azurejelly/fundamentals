package dev.azuuure.fundamentals.loader.storage;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.loader.Loader;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.factory.UserStorageFactory;

import java.util.logging.Level;

public class StorageLoader implements Loader {

    private final Fundamentals plugin;
    private final UserStorageFactory factory;

    public StorageLoader(Fundamentals plugin) {
        this.plugin = plugin;
        this.factory = new UserStorageFactory(plugin, plugin.getConfig());
    }

    @Override
    public void load() {
        try {
            plugin.setUserStorage(factory.create());
            plugin.getUserStorage().init();
        } catch (StorageInitializationException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize storage", ex);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}
