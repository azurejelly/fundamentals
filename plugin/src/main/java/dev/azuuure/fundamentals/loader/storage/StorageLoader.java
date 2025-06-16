package dev.azuuure.fundamentals.loader.storage;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.loader.Loader;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.exception.StorageShutdownException;
import dev.azuuure.fundamentals.api.storage.factory.UserStorageFactory;

import java.util.logging.Level;

public class StorageLoader implements Loader {

    private final Fundamentals plugin;

    public StorageLoader(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        UserStorageFactory factory = new UserStorageFactory(plugin);

        try {
            plugin.setUserStorage(factory.create());
            plugin.getUserStorage().init();
        } catch (StorageInitializationException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize user storage", ex);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public void unload() {
        if (plugin.getUserStorage() == null) {
            return;
        }

        try {
            plugin.getUserStorage().shutdown();
        } catch (StorageShutdownException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to shutdown user storage", ex);
        }
    }
}
