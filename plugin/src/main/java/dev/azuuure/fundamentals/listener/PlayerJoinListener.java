package dev.azuuure.fundamentals.listener;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.user.User;
import dev.azuuure.fundamentals.api.util.LocationUtils;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    private final Fundamentals plugin;

    public PlayerJoinListener(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getBoolean("config.join.custom-join-message", true)) {
            Server server = plugin.getServer();
            event.joinMessage(
                    plugin.getMessages().getComponent("alerts.player-join",
                            Placeholder.unparsed("name", player.getName()),
                            Placeholder.unparsed("online_players", String.valueOf(server.getOnlinePlayers().size())),
                            Placeholder.unparsed("max_players", String.valueOf(server.getMaxPlayers()))
                    )
            );
        }

        if (plugin.getConfig().getBoolean("config.join.teleport-to-spawn", false)) {
            try {
                player.teleport(LocationUtils.deserialize(plugin.getConfig().getString("config.spawn")));
            } catch (RuntimeException ex) {
                player.sendMessage(plugin.getMessages().getComponent("commands.spawn.invalid-spawn"));
                plugin.getLogger().log(Level.SEVERE, "Failed to deserialize spawn location", ex);
            }
        }

        if (plugin.getConfig().getBoolean("config.join.show-welcome-message", true)) {
            boolean hasMiniPlaceholders = plugin.getServer()
                    .getPluginManager()
                    .isPluginEnabled("MiniPlaceholders");

            TagResolver resolver = hasMiniPlaceholders
                    ? MiniPlaceholders.getAudiencePlaceholders(player)
                    : TagResolver.empty();

            player.sendMessage(
                    plugin.getMessages().getComponent(
                            "welcome-message", resolver,
                            Placeholder.unparsed("world", player.getWorld().getName()),
                            Placeholder.unparsed("name", player.getName())
                    )
            );
        }
    }
}
