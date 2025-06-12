package dev.azuuure.fundamentals.command.spawn;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.util.LocationUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;

@Command(value = "spawn", alias = { "fspawn" })
public class SpawnCommand extends BaseCommand {

    private final Fundamentals plugin;

    public SpawnCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.spawn")
    public void execute(Player player, @Optional Player target) {
        String serializedSpawn = plugin.getConfig().getString("config.spawn");
        if (serializedSpawn == null || serializedSpawn.isEmpty()) {
            player.sendMessage(plugin.getMessages().getComponent("commands.spawn.no-spawn"));
            return;
        }

        if (target != null) {
            if (!player.hasPermission("fundamentals.command.spawn.other")) {
                player.sendMessage(plugin.getMessages().getComponent("commands.general.no-permission"));
                return;
            }

            if (!target.isOnline()) {
                player.sendMessage(plugin.getMessages().getComponent("commands.general.offline"));
                return;
            }
        } else {
            target = player;
        }

        try {
            Location location = LocationUtils.deserialize(serializedSpawn);
            target.teleport(location);
        } catch (RuntimeException ex) {
            player.sendMessage(plugin.getMessages().getComponent("commands.spawn.invalid-spawn"));
            plugin.getLogger().log(Level.SEVERE, "Failed to deserialize spawn location", ex);
            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getMessages().getComponent("commands.spawn.self"));
        } else {
            player.sendMessage(
                    plugin.getMessages().getComponent("commands.spawn.teleported-by-admin",
                            Placeholder.unparsed("administrator", player.getName())
                    )
            );

            player.sendMessage(
                    plugin.getMessages().getComponent("commands.spawn.other",
                            Placeholder.unparsed("target", target.getName())
                    )
            );
        }
    }

    @SubCommand("set")
    @Permission("fundamentals.command.spawn.set")
    public void setSpawn(Player player) {
        Location location = player.getLocation();
        String serializedLocation = LocationUtils.serialize(location);

        try {
            plugin.getConfig().set("config.spawn", serializedLocation);
            plugin.getConfig().save();
        } catch (IOException e) {
            player.sendMessage(plugin.getMessages().getComponent("commands.spawn.spawn-set-fail"));
            plugin.getLogger().log(Level.SEVERE, "Failed to save spawn location", e);
            return;
        }

        player.sendMessage(plugin.getMessages().getComponent("commands.spawn.spawn-set"));
    }
}
