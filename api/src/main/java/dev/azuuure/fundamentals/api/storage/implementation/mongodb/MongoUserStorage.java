package dev.azuuure.fundamentals.api.storage.implementation.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.azuuure.fundamentals.api.jackson.factory.ObjectMapperFactory;
import dev.azuuure.fundamentals.api.storage.UserStorage;
import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.exception.StorageShutdownException;
import dev.azuuure.fundamentals.api.storage.implementation.mongodb.settings.MongoStorageSettings;
import dev.azuuure.fundamentals.api.user.User;
import org.bson.UuidRepresentation;
import org.bukkit.plugin.Plugin;
import org.mongojack.JacksonMongoCollection;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class MongoUserStorage implements UserStorage {

    private final Plugin plugin;
    private final MongoStorageSettings settings;
    private Map<UUID, User> cache;
    private JacksonMongoCollection<User> collection;
    private MongoClient mongoClient;

    public MongoUserStorage(Plugin plugin, MongoStorageSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @Override
    public void init() throws StorageInitializationException {
        String database = settings.getDatabase();

        if (database == null || database.trim().isEmpty()) {
            throw new IllegalArgumentException("Database cannot be null or empty");
        }

        if (settings.getURI() != null && !settings.getURI().trim().isEmpty()) {
            this.mongoClient = MongoClients.create(settings.getURI());
        } else {
            ServerAddress address = new ServerAddress(settings.getHostname(), settings.getPort());
            String authDatabase = settings.getAuthDatabase();
            String username = settings.getUsername();
            char[] password = settings.getPassword();

            if (username != null && !username.trim().isEmpty() && password != null) {
                this.mongoClient = MongoClients.create(MongoClientSettings.builder()
                        .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                        .applyToClusterSettings(builder -> builder.hosts(List.of(address)))
                        .credential(MongoCredential.createCredential(username, authDatabase, password))
                        .build());
            } else {
                this.mongoClient = MongoClients.create(MongoClientSettings.builder()
                        .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                        .applyToClusterSettings(builder -> builder.hosts(List.of(address)))
                        .build());
            }
        }

        this.cache = new ConcurrentHashMap<>();
        this.collection = JacksonMongoCollection.builder()
                .withObjectMapper(ObjectMapperFactory.create())
                .build(mongoClient, database, "users", User.class, UuidRepresentation.JAVA_LEGACY);

        plugin.getLogger().info("Using MongoDB storage implementation.");
    }

    @Override
    public User loadUser(UUID uuid) {
        return collection.findOneById(uuid);
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
        if (user == null || user.getUUID() == null) {
            throw new IllegalArgumentException("User and UUID must not be null");
        }

        try {
            collection.save(user);
            cache.put(user.getUUID(), user);
        } catch (MongoException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save user " + user.getUUID(), ex);
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }

        try {
            collection.removeById(uuid);
            cache.remove(uuid);
        } catch (MongoException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete user " + uuid, ex);
        }
    }

    @Override
    public Map<UUID, User> getAllLoaded() {
        return cache;
    }

    @Override
    public void shutdown() throws StorageShutdownException {
        this.plugin.getLogger().info("Shutting down MongoDB storage implementation...");
        this.cache.forEach((uuid, user) -> saveUser(user));

        if (this.mongoClient != null) {
            try {
                this.mongoClient.close();
            } catch (RuntimeException ex) {
                throw new StorageShutdownException("Failed to close MongoDB client", ex);
            }
        }

        this.cache.clear();
    }
}
