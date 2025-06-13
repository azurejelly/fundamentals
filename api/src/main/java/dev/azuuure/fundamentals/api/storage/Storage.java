package dev.azuuure.fundamentals.api.storage;

import dev.azuuure.fundamentals.api.storage.implementation.StorageImplementation;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public final class Storage {

    private final Plugin plugin;
    private final StorageImplementation implementation;

    public Storage(Plugin plugin, StorageImplementation implementation) {
        this.plugin = plugin;
        this.implementation = implementation;
    }

    public void init() {
        try {
            implementation.init();
        } catch (RuntimeException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize storage", ex);
        }
    }

    public void shutdown() {
        try {
            implementation.shutdown();
        } catch (RuntimeException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to shutdown storage", ex);
        }
    }

    public StorageImplementation getImplementation() {
        return implementation;
    }
}
