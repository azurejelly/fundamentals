package dev.azuuure.fundamentals.command.clear;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("clear")
public class ClearCommand extends BaseCommand {

    private final Fundamentals plugin;

    public ClearCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.clear")
    public void execute(CommandSender sender, @Optional Player target) {
        if (sender instanceof Player player) {
            if (target == null) {
                target = player;
            } else {
                if (!player.hasPermission("fundamentals.command.clear.other")) {
                    player.sendMessage(plugin.getMessages().getComponent("commands.general.no-permission"));
                    return;
                }
            }

            if (player.getUniqueId().equals(target.getUniqueId())) {
                player.sendMessage(plugin.getMessages().getComponent("commands.clear.success"));
            }
        } else {
            if (target == null) {
                sender.sendMessage(plugin.getMessages().getComponent("commands.general.must-provide-player"));
                return;
            }

            target.sendMessage(
                    plugin.getMessages().getComponent("commands.clear.cleared-by-admin",
                            Placeholder.unparsed("administrator", sender.getName())
                    )
            );

            sender.sendMessage(
                    plugin.getMessages().getComponent("commands.clear.success-other",
                            Placeholder.unparsed("target", target.getName())
                    )
            );
        }

        target.getInventory().clear();
        target.getInventory().setArmorContents(null);
    }
}
