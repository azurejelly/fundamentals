package dev.azuuure.fundamentals.api.storage.factory;

import dev.azuuure.fundamentals.api.file.YamlFile;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.UserStorage;
import dev.azuuure.fundamentals.api.storage.implementation.file.FileUserStorage;
import dev.azuuure.fundamentals.api.storage.implementation.mongodb.MongoUserStorage;
import dev.azuuure.fundamentals.api.storage.implementation.mongodb.settings.MongoStorageSettings;
import dev.azuuure.fundamentals.api.storage.type.StorageType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public final class UserStorageFactory {

    private final Plugin plugin;
    private final FileConfiguration config;

    public UserStorageFactory(Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public UserStorage create() {
        String name = config.getString("config.storage.type", "FILE").toUpperCase();
        StorageType type;

        try {
            type = StorageType.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new StorageInitializationException("Invalid storage type '" + name + "'");
        }

        switch (type) {
            case FILE: {
                return new FileUserStorage(plugin);
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

                return new MongoUserStorage(plugin, settings);
            }
            default: {
                throw new StorageInitializationException("Unsupported storage type: " + type);
            }
        }
    }
}
