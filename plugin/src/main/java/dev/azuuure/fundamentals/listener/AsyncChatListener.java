package dev.azuuure.fundamentals.listener;

import dev.azuuure.fundamentals.Fundamentals;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {

    private final Fundamentals plugin;

    public AsyncChatListener(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncChatEvent event) {
        if (!plugin.getConfig().getBoolean("config.utilities.block-mobile-ads.enabled", true)) {
            return;
        }

        Player player = event.getPlayer();
        String msg = event.message().toString();

        if (msg.contains("pickaxechat") || msg.contains("minechat") || msg.contains("chatcraft")) {
            event.setCancelled(true);
        }

        if (plugin.getConfig().getBoolean("config.utilities.block-mobile-ads.send-alert", true)) {
            player.sendMessage(plugin.getMessages().getComponent("alerts.mobile-ad-blocked"));
        }
    }
}
