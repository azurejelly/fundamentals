package dev.azuuure.fundamentals.api.storage.factory;

import dev.azuuure.fundamentals.api.file.YamlFile;
import dev.azuuure.fundamentals.api.storage.Storage;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.implementation.file.FileStorageImplementation;
import dev.azuuure.fundamentals.api.storage.type.StorageType;
import org.bukkit.plugin.Plugin;

public class StorageFactory {

    private final Plugin plugin;
    private final YamlFile config;

    public StorageFactory(Plugin plugin, YamlFile config) {
        this.plugin = plugin;
        this.config = config;
    }

    public Storage create() {
        String name = config.getString("config.storage.type", "YAML").toUpperCase();
        StorageType type;

        try {
            type = StorageType.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new StorageInitializationException("Invalid storage type '" + name + "'");
        }

        switch (type) {
            case FILE: {
                try {
                    return new Storage(plugin, new FileStorageImplementation(plugin));
                } catch (RuntimeException e) {
                    throw new StorageInitializationException("Failed to initialize YAML storage", e);
                }
            }
            case MONGODB: {
                throw new UnsupportedOperationException();
            }
            default: {
                throw new StorageInitializationException("Unsupported storage type: " + type);
            }
        }
    }
}
