package dev.azuuure.fundamentals.command.workbench;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "workbench", alias = { "fworkbench", "craftingtable", "fcraftingtable", "craft", "fcraft" })
public class WorkbenchCommand extends BaseCommand {

    private final Fundamentals plugin;

    public WorkbenchCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.workbench")
    public void execute(CommandSender sender, @Optional Player target) {
        if (sender instanceof Player player) {
            if (target == null) {
                target = player;
            } else {
                if (!player.hasPermission("fundamentals.command.workbench.other")) {
                    player.sendMessage(plugin.getMessages().getComponent("commands.general.no-permission"));
                    return;
                }
            }
        } else {
            if (target == null) {
                sender.sendMessage(plugin.getMessages().getComponent("commands.general.must-provide-player"));
                return;
            }
        }

        target.openWorkbench(null, true);
        target.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.workbench.other",
                        Placeholder.unparsed("target", target.getName())
                )
        );
    }
}
