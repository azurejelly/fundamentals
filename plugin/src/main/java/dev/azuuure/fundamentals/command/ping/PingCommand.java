package dev.azuuure.fundamentals.command.ping;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "ping", alias = { "fping", "latency", "flatency" })
public class PingCommand extends BaseCommand {

    private final Fundamentals plugin;

    public PingCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.ping")
    public void execute(CommandSender sender, @Optional Player target) {
        boolean self;

        if (sender instanceof Player player) {
            if (target == null) {
                target = player;
            } else {
                if (!player.hasPermission("fundamentals.command.ping.other")) {
                    player.sendMessage(plugin.getMessages().getComponent("commands.general.no-permission"));
                    return;
                }
            }

            self = target.getUniqueId().equals(player.getUniqueId());
        } else {
            if (target == null) {
                sender.sendMessage(plugin.getMessages().getComponent("commands.general.must-provide-player"));
                return;
            }

            self = false;
        }

        var ping = target.getPing();
        sender.sendMessage(
                plugin.getMessages().getComponent("commands.ping." + (self ? "self" : "other"),
                        Placeholder.unparsed("ping", String.valueOf(ping)),
                        Placeholder.parsed("ping_color", colorFor(ping)),
                        Placeholder.unparsed("player", target.getName())
                )
        );
    }

    private String colorFor(int ping) {
        if (ping < 100) {
            return plugin.getConfig().getString("config.ping.good", "<color:green>");
        } else if (ping < 200) {
            return plugin.getConfig().getString("config.ping.average", "<color:gold>");
        } else {
            return plugin.getConfig().getString("config.ping.bad", "<color:red>");
        }
    }
}
