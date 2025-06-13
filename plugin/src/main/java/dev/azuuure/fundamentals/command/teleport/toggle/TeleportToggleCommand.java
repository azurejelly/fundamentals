package dev.azuuure.fundamentals.command.teleport.toggle;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.user.User;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "tptoggle", alias = {
        "teleporttoggle",
        "toggleteleport",
        "toggletp",
        "ftptoggle",
        "fteleporttoggle",
        "ftoggleteleport",
        "ftoggletp"
})
public class TeleportToggleCommand extends BaseCommand {

    private final Fundamentals plugin;

    public TeleportToggleCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.teleport.toggle")
    public void execute(CommandSender sender, @Optional Player target) {
        boolean self;

        if (sender instanceof Player player) {
            if (target == null) {
                target = player;
            } else {
                if (!player.hasPermission("fundamentals.command.teleport.toggle.other")) {
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

        User user = plugin.getStorage().getImplementation().getUser(target.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.general.user-failure"));
            return;
        }

        user.setAllowsTeleport(!user.isAllowsTeleport());

        if (self) {
            sender.sendMessage(
                    plugin.getMessages().getComponent(
                            "commands.teleport.toggle.self."
                                    + (user.isAllowsTeleport() ? "enabled" : "disabled")
                    )
            );
        } else {
            sender.sendMessage(
                    plugin.getMessages().getComponent(
                            "commands.teleport.toggle.other."
                                    + (user.isAllowsTeleport() ? "enabled" : "disabled"),
                            Placeholder.unparsed("target", target.getName())
                    )
            );

            target.sendMessage(
                    plugin.getMessages().getComponent(
                            "commands.teleport.toggle.other."
                                    + (user.isAllowsTeleport() ? "enabled" : "disabled")
                                    + "by-admin",
                            Placeholder.unparsed("administrator", sender.getName())
                    )
            );
        }
    }
}
