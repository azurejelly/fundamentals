package dev.azuuure.fundamentals.command.top;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "top", alias = { "ftop", "fhighest", "highest" })
public class TopCommand extends BaseCommand {

    private final Fundamentals plugin;

    public TopCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.top")
    public void execute(CommandSender sender, @Optional Player target) {
        if (sender instanceof Player player) {
            if (target == null) {
                target = player;
            } else {
                if (!player.hasPermission("fundamentals.command.top.other")) {
                    player.sendMessage(plugin.getMessages().getComponent("commands.general.no-permission"));
                    return;
                }
            }

            if (target.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(plugin.getMessages().getComponent("commands.top.self"));
            }
        } else {
            if (target == null) {
                sender.sendMessage(plugin.getMessages().getComponent("commands.general.must-provide-player"));
                return;
            }

            target.sendMessage(
                    plugin.getMessages().getComponent(
                            "commands.top.teleported-by-admin",
                            Placeholder.unparsed("administrator", sender.getName())
                    )
            );

            sender.sendMessage(
                    plugin.getMessages().getComponent(
                            "commands.top.other", Placeholder.unparsed("target", target.getName())
                    )
            );
        }

        // Get the highest block location at the target player's current position
        Location previous = target.getLocation();
        Location highestPoint = target.getWorld()
                .getHighestBlockAt(previous)
                .getLocation()
                .add(0.5, 1, 0.5);

        // Preserve the player's previous yaw and pitch
        highestPoint.setYaw(previous.getYaw());
        highestPoint.setPitch(previous.getPitch());

        // Teleport the target player to the highest point
        target.teleport(highestPoint);
    }
}
