package dev.azuuure.fundamentals.loader.config;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.Loader;
import dev.azuuure.fundamentals.api.file.YamlFile;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.logging.Level;

public class ConfigurationLoader implements Loader {

    private final Fundamentals plugin;

    public ConfigurationLoader(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        try {
            plugin.setConfig(new YamlFile(plugin, "config.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load configuration", e);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        try {
            plugin.setMessages(new YamlFile(plugin, "messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load messages", e);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}
