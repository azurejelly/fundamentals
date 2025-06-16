package dev.azuuure.fundamentals.listener;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.user.User;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Fundamentals plugin;

    public PlayerQuitListener(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserStorage().getUser(player.getUniqueId());

        if (user != null) {
            user.refresh();
            plugin.getUserStorage().saveUser(user);
        }

        if (plugin.getConfig().getBoolean("config.quit.custom-quit-message", true)) {
            Server server = plugin.getServer();
            event.quitMessage(
                    plugin.getMessages().getComponent("alerts.player-leave",
                            Placeholder.unparsed("name", player.getName()),
                            Placeholder.unparsed("online_players", String.valueOf(server.getOnlinePlayers().size())),
                            Placeholder.unparsed("max_players", String.valueOf(server.getMaxPlayers()))
                    )
            );
        }
    }
}
