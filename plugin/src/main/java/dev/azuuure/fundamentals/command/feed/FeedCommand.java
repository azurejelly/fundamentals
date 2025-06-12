package dev.azuuure.fundamentals.command.feed;


import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "feed", alias = { "ffeed", "food", "ffood" })
public class FeedCommand extends BaseCommand {

    private final Fundamentals plugin;

    public FeedCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.feed")
    public void execute(Player player, @Optional Player target) {
        if (target != null) {
            if (!player.hasPermission("fundamentals.command.feed.other")) {
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

        target.setFoodLevel(20);

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getMessages().getComponent("commands.feed.self"));
            return;
        }

        target.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.feed.fed-by-admin", Placeholder.unparsed("administrator", player.getName())
                )
        );

        player.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.feed.other", Placeholder.unparsed("target", target.getName())
                )
        );
    }
}
