package dev.azuuure.fundamentals.listener;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.net.InetAddress;
import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {

    private final Fundamentals plugin;

    public AsyncPlayerPreLoginListener(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        InetAddress address = event.getAddress();
        User user = plugin.getUserStorage().loadOrCreate(uuid, name);

        if (user != null) {
            user.setLastKnownName(name);
            user.setLastAddress(address);
            user.updateLastSeen();
            return;
        }

        if (plugin.getConfig().getBoolean("config.storage.kick-on-load-fail", true)) {
            event.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    plugin.getMessages().getComponent("errors.data-load-fail")
            );
        }
    }
}
