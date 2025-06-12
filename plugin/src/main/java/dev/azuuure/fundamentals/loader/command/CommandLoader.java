package dev.azuuure.fundamentals.loader.command;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.Loader;
import dev.azuuure.fundamentals.command.FundamentalsCommand;
import dev.azuuure.fundamentals.command.broadcast.BroadcastCommand;
import dev.azuuure.fundamentals.command.discord.DiscordCommand;
import dev.azuuure.fundamentals.command.feed.FeedCommand;
import dev.azuuure.fundamentals.command.heal.HealCommand;
import dev.azuuure.fundamentals.command.ping.PingCommand;
import dev.azuuure.fundamentals.command.spawn.SpawnCommand;
import dev.azuuure.fundamentals.command.top.TopCommand;
import dev.azuuure.fundamentals.command.website.WebsiteCommand;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.message.context.MessageContext;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

public class CommandLoader implements Loader {

    private final Fundamentals plugin;
    private final BukkitCommandManager<CommandSender> commandManager;

    public CommandLoader(Fundamentals plugin) {
        this.plugin = plugin;
        this.commandManager = BukkitCommandManager.create(plugin);
    }

    @Override
    public void load() {
        commandManager.registerCommand(new BroadcastCommand(plugin));
        commandManager.registerCommand(new DiscordCommand(plugin));
        commandManager.registerCommand(new FeedCommand(plugin));
        commandManager.registerCommand(new HealCommand(plugin));
        commandManager.registerCommand(new PingCommand(plugin));
        commandManager.registerCommand(new SpawnCommand(plugin));
        commandManager.registerCommand(new TopCommand(plugin));
        commandManager.registerCommand(new WebsiteCommand(plugin));
        commandManager.registerCommand(new FundamentalsCommand(plugin));
    }

    private void configureMessage(BukkitMessageKey<? extends MessageContext> key, String path) {
        commandManager.registerMessage(key, (sender, context) ->
                sender.sendMessage(
                        plugin.getMessages().getComponent(path,
                                Placeholder.unparsed("command", context.getCommand()),
                                Placeholder.unparsed("subcommand", context.getSubCommand())
                        )
                )
        );
    }

    private void configureMessage(MessageKey<? extends MessageContext> key, String path) {
        commandManager.registerMessage(key, (sender, context) ->
                sender.sendMessage(
                        plugin.getMessages().getComponent(path,
                                Placeholder.unparsed("command", context.getCommand()),
                                Placeholder.unparsed("subcommand", context.getSubCommand())
                        )
                )
        );
    }
}
