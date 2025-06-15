package dev.azuuure.fundamentals.api.storage.factory;

import dev.azuuure.fundamentals.api.file.YamlFile;
import dev.azuuure.fundamentals.api.storage.Storage;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.implementation.file.FileStorageImplementation;
import dev.azuuure.fundamentals.api.storage.implementation.mongodb.MongoStorageImplementation;
import dev.azuuure.fundamentals.api.storage.implementation.mongodb.settings.MongoStorageSettings;
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
                String path = "config.storage.mongodb.";
                MongoStorageSettings settings = MongoStorageSettings.builder()
                        .uri(plugin.getConfig().getString(path + "uri"))
                        .hostname(plugin.getConfig().getString(path + "hostname", "localhost"))
                        .port(plugin.getConfig().getInt(path + "port", 27017))
                        .username(plugin.getConfig().getString(path + "username"))
                        .password(plugin.getConfig().getString(path + "password"))
                        .database(plugin.getConfig().getString(path + "database", "fundamentals"))
                        .authDatabase(plugin.getConfig().getString(path + "auth-database", "admin"))
                        .build();

                try {
                    return new Storage(plugin, new MongoStorageImplementation(plugin, settings));
                } catch (RuntimeException ex) {
                    throw new StorageInitializationException("Failed to initialize MongoDB storage", ex);
                }
            }
            default: {
                throw new StorageInitializationException("Unsupported storage type: " + type);
            }
        }
    }
}
