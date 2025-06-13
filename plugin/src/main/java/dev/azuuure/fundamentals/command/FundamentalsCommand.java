package dev.azuuure.fundamentals.command;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.logging.Level;

@Command(value = "fundamentals", alias = "fundamental")
public class FundamentalsCommand extends BaseCommand {

    private final Fundamentals plugin;

    public FundamentalsCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.main")
    @SuppressWarnings("UnstableApiUsage")
    public void execute(CommandSender sender) {
        sender.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.main.default",
                        Placeholder.unparsed("version", plugin.getPluginMeta().getVersion()),
                        Placeholder.unparsed("authors", String.join(", ", plugin.getPluginMeta().getAuthors()))
                )
        );
    }

    @SubCommand("reload")
    @Permission("fundamentals.command.reload")
    public void reload(CommandSender sender) {
        try {
            plugin.getConfig().reload();
            plugin.getMessages().reload();
            plugin.getLogger().info("Fundamentals configuration has been reloaded.");
            sender.sendMessage(plugin.getMessages().getComponent("commands.main.reload.success"));
        } catch (IOException | InvalidConfigurationException e) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.main.reload.fail"));
            plugin.getLogger().log(Level.SEVERE, "Reload failed", e);
        }
    }
}
