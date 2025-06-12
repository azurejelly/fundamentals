package dev.azuuure.fundamentals.command.broadcast;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Join;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command(value = "broadcast", alias = { "alert", "fbroadcast", "falert", "bc", "fbc" })
public class BroadcastCommand extends BaseCommand {

    private final Fundamentals plugin;

    public BroadcastCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.broadcast")
    public void execute(CommandSender sender, @Join String content) {
        boolean legacy = plugin.getConfig().getBoolean("config.broadcast.prefer-legacy-color-system", true);

        if (content.trim().isEmpty()) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.broadcast.blank"));
            return;
        }

        if (legacy) {
            //noinspection deprecation
            content = ChatColor.translateAlternateColorCodes('&', content);

            Bukkit.broadcast(
                    plugin.getMessages().getComponent("commands.broadcast.prefix")
                            .append(Component.text(content))
            );
        } else {
            Bukkit.broadcast(
                    plugin.getMessages().getComponent("commands.broadcast.prefix")
                            .append(MiniMessage.miniMessage().deserialize(content))
            );
        }
    }
}
