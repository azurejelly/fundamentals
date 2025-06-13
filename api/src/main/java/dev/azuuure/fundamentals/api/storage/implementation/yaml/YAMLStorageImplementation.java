package dev.azuuure.fundamentals.api.storage.implementation.yaml;

import dev.azuuure.fundamentals.api.file.YamlFile;
import dev.azuuure.fundamentals.api.storage.implementation.StorageImplementation;
import dev.azuuure.fundamentals.api.user.User;
import dev.azuuure.fundamentals.api.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class YAMLStorageImplementation implements StorageImplementation {

    private final Plugin plugin;
    private final Map<UUID, User> cache;

    public YAMLStorageImplementation(Plugin plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public void init() {
        plugin.getLogger().info("Using YAML storage implementation.");
    }

    @Override
    public User loadUser(UUID uuid) {
        User loaded = getUser(uuid);
        if (loaded != null) {
            return loaded;
        }

        try {
            YamlFile file = new YamlFile(plugin, "users/" + uuid + ".yml");
            if (file.getConfigurationSection("data") == null) {
                Player player = plugin.getServer().getPlayer(uuid);
                User user = player != null && player.isOnline()
                        ? new User(player)
                        : new User(uuid);

                saveUser(user);
                cache.put(uuid, user);
                return user;
            }

            String serializedLocation = file.getString("data.lastKnownLocation", null);
            Location lastKnownLocation = serializedLocation != null && !serializedLocation.trim().isEmpty()
                    ? LocationUtils.deserialize(serializedLocation)
                    : null;

            User user = new User(
                    uuid,
                    file.getString("data.lastKnownName", "Unknown"),
                    lastKnownLocation,
                    file.getString("data.lastAddress", null),
                    file.getLong("data.firstSeen", System.currentTimeMillis()),
                    file.getLong("data.lastSeen", System.currentTimeMillis())
            );

            cache.put(uuid, user);
            return user;
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load user data for " + uuid, e);
            return null;
        }
    }

    @Override
    public User getUser(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public void saveUser(User user) {
        try {
            YamlFile file = new YamlFile(plugin, "users/" + user.getUUID() + ".yml");

            user.serialize().forEach((key, value) -> {
                file.set("data." + key, value);
            });

            file.save();
            cache.put(user.getUUID(), user);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save user data for " + user.getUUID(), e);
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        File file = new File(plugin.getDataFolder(), "users/" + uuid + ".yml");
        if (file.exists() && !file.delete()) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete user file for " + uuid);
        }

        cache.remove(uuid);
    }

    @Override
    public Map<UUID, User> getAllLoaded() {
        return cache;
    }

    @Override
    public void shutdown() {
        // ...
    }
}
