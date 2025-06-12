package dev.azuuure.fundamentals.api.file;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Represents a YAML configuration file located at the {@link Plugin}'s data folder.
 *
 * @author azurejelly
 */
public class YamlFile extends YamlConfiguration {

    /**
     * The {@link File} that we will use for the YAML configuration.
     */
    private final File file;

    /**
     * The {@link Plugin} that will own this YAML configuration.
     */
    private final Plugin plugin;

    /**
     * The {@link MiniMessage} instance to use in order to deserialize strings.
     */
    private final MiniMessage miniMessage;

    /**
     * Creates a new {@link YamlFile} instance and automatically loads it.
     * If the file hasn't been created yet, it uses the default values obtained
     * from {@link Plugin#getResource(String)}.
     *
     * @param plugin A {@link Plugin} instance, used to obtain its data folder.
     * @param name The name of the {@link File} that will be loaded as a YAML configuration.
     * @throws IOException When an I/O error is thrown.
     * @throws InvalidConfigurationException When the configuration file contains an invalid syntax.
     */
    public YamlFile(Plugin plugin, String name) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        this.file = new File(plugin.getDataFolder(),
                (name.endsWith(".yml") || name.endsWith(".yaml") ? name : name + ".yml")
        );

        this.load();
    }

    /**
     * Loads the {@link YamlFile YAML configuration}.
     * If the file hasn't been created yet, it uses the default
     * values obtained from {@link Plugin#getResource(String)}.
     *
     * @throws IOException When an I/O error is thrown.
     * @throws InvalidConfigurationException When the configuration file contains an invalid syntax.
     */
    public void load() throws IOException, InvalidConfigurationException {
        if (file.exists()) {
            load(file);
            save(file);
        } else {
            if (plugin.getResource(file.getName()) != null) {
                plugin.saveResource(file.getName(), false);
            } else {
                save(file);
            }

            load(file);
        }
    }

    /**
     * Saves changes made to this {@link YamlFile YAML configuration} to disk.
     *
     * @throws IOException When an I/O error is thrown.
     */
    public void save() throws IOException {
        save(file);
    }

    /**
     * Re-loads this {@link YamlFile YAML configuration} from disk. <b>Changes made
     * to the file in memory will be lost unless {@link #save()} is called beforehand.</b>
     *
     * @throws IOException When an I/O error is thrown.
     * @throws InvalidConfigurationException When the configuration file contains an invalid syntax.
     */
    public void reload() throws IOException, InvalidConfigurationException {
        load(file);
    }

    /**
     * Automatically deserializes the content from {@link YamlConfiguration#getString(String)}
     * using {@link MiniMessage#deserialize(Object)}.
     *
     * @param path The path of the {@link String} to deserialize in this YAML file.
     * @return The deserialized {@link String} at the provided path, which is provided as a {@link Component}.
     */
    public Component getComponent(@NotNull String path) {
        String res = super.getString(path);

        if (res == null) {
            return Component.empty();
        }

        return miniMessage.deserialize(res);
    }

    /**
     * Automatically deserializes the content from {@link YamlConfiguration#getString(String)}
     * using {@link MiniMessage#deserialize(Object)}.
     *
     * @param path The path of the {@link String} to deserialize in this YAML file.
     * @param placeholders Placeholders which should be used when deserializing the message.
     * @return The deserialized {@link String} at the provided path, which is provided as a {@link Component}.
     */
    public Component getComponent(@NotNull String path, TagResolver... placeholders) {
        String res = super.getString(path);

        if (res == null) {
            return Component.empty();
        }

        return miniMessage.deserialize(res, placeholders);
    }

    /**
     * Returns the {@link File} that is being used by this {@link YamlFile} instance.
     * @return The {@link File} of this {@link YamlFile} instance.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Returns the {@link File folder} where the {@link #getFile() file} of this {@link YamlFile}
     * instance is located.
     *
     * @return The {@link File folder} of the {@link #getFile() file} of this {@link YamlFile} instance.
     */
    public File getFolder() {
        return plugin.getDataFolder();
    }
}