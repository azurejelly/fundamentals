package dev.azuuure.fundamentals.api.storage.implementation.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.azuuure.fundamentals.api.jackson.factory.ObjectMapperFactory;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.UserStorage;
import dev.azuuure.fundamentals.api.user.User;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class FileUserStorage implements UserStorage {

    private final Plugin plugin;
    private final Map<UUID, User> cache;
    private final boolean verbose;
    private File userDirectory;
    private ObjectMapper mapper;

    public FileUserStorage(Plugin plugin, boolean verbose) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
        this.verbose = verbose;
    }

    public FileUserStorage(Plugin plugin) {
        this(plugin, false);
    }

    @Override
    public void init() {
        this.plugin.getLogger().info("Initializing JSON storage implementation.");
        this.mapper = ObjectMapperFactory.create();
        this.userDirectory = new File(plugin.getDataFolder(), "users");

        if (!userDirectory.exists() && !userDirectory.mkdirs()) {
            throw new StorageInitializationException("Failed to make user data directory");
        }
    }

    @Override
    public User loadUser(UUID uuid) {
        File file = new File(userDirectory, uuid + ".json");
        if (!file.exists()) {
            return null;
        }

        try {
            User user = mapper.readValue(file, User.class);
            cache.put(uuid, user);
            return user;
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load user " + uuid, ex);
            return null;
        }
    }

    @Override
    public User loadOrCreate(UUID uuid, String name) {
        User user = loadUser(uuid);
        if (user != null) {
            return user;
        } else {
            user = new User(uuid, name);
            cache.put(uuid, user);
            return user;
        }
    }

    @Override
    public User getUser(UUID uuid) {
        return cache.computeIfAbsent(uuid, this::loadUser);
    }

    @Override
    public void saveUser(User user) {
        File file = new File(userDirectory, user.getUUID() + ".json");

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, user);
            cache.put(user.getUUID(), user);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save data for " + user.getUUID(), ex);
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        File file = new File(userDirectory, uuid + ".json");
        if (file.exists() && file.delete()) {
            cache.remove(uuid);
            if (verbose) {
                plugin.getLogger().info("Deleted user file for " + uuid);
            }
        } else {
            plugin.getLogger().warning("Failed to delete user file for " + uuid);
        }
    }

    @Override
    public Map<UUID, User> getAllLoaded() {
        return this.cache;
    }

    @Override
    public void shutdown() {
        int loaded = this.getAllLoaded().size();
        this.plugin.getLogger().info("Shutting down JSON storage implementation. This could take a while.");
        this.getAllLoaded().forEach((id, user) -> saveUser(user));
        this.plugin.getLogger().info("Saved " + loaded + " user(s).");
    }
}
