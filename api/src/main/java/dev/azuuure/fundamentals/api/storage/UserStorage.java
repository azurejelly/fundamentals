package dev.azuuure.fundamentals.api.storage;

import dev.azuuure.fundamentals.api.storage.exception.StorageInitializationException;
import dev.azuuure.fundamentals.api.storage.exception.StorageShutdownException;
import dev.azuuure.fundamentals.api.user.User;

import java.util.Map;
import java.util.UUID;

public interface UserStorage {

    void init() throws StorageInitializationException;

    User loadUser(UUID uuid);

    User loadOrCreate(UUID uuid, String name);

    User getUser(UUID uuid);

    void saveUser(User user);

    void deleteUser(UUID uuid);

    default void deleteUser(User user) {
        deleteUser(user.getUUID());
    }

    Map<UUID, User> getAllLoaded();

    void shutdown() throws StorageShutdownException;
}
