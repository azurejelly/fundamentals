package dev.azuuure.fundamentals;

import dev.azuuure.fundamentals.api.loader.Loader;
import dev.azuuure.fundamentals.api.file.YamlFile;
import dev.azuuure.fundamentals.api.storage.UserStorage;
import dev.azuuure.fundamentals.loader.command.CommandLoader;
import dev.azuuure.fundamentals.loader.config.ConfigurationLoader;
import dev.azuuure.fundamentals.loader.listener.ListenerLoader;
import dev.azuuure.fundamentals.loader.storage.StorageLoader;
import dev.azuuure.fundamentals.loader.vault.VaultLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class Fundamentals extends JavaPlugin {

    private Set<Loader> loaders;
    private YamlFile config, messages;
    private UserStorage userStorage;

    @Override
    public void onLoad() {
        this.loaders = new LinkedHashSet<>();
        this.loaders.add(new ConfigurationLoader(this));
        this.loaders.add(new CommandLoader(this));
        this.loaders.add(new ListenerLoader(this));
        this.loaders.add(new StorageLoader(this));
        this.loaders.add(new VaultLoader(this));
    }

    @Override
    public void onEnable() {
        loaders.forEach(Loader::load);
        getLogger().info(getDescription().getName() + " version " + getDescription().getVersion() + " has been enabled.");
    }

    @Override
    public void onDisable() {
        loaders.forEach(Loader::unload);
        getLogger().info(getDescription().getName() + " version " + getDescription().getVersion() + " has been disabled.");
    }

    @Override
    public @NotNull YamlFile getConfig() {
        return config;
    }

    public YamlFile getMessages() {
        return messages;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void setConfig(YamlFile config) {
        this.config = config;
    }

    public void setMessages(YamlFile messages) {
        this.messages = messages;
    }
}