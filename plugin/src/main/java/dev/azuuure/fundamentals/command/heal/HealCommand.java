package dev.azuuure.fundamentals.command.heal;


import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

@Command(value = "heal", alias = { "fheal" })
public class HealCommand extends BaseCommand {

    private final Fundamentals plugin;

    public HealCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.heal")
    public void execute(Player player, @Optional Player target) {
        if (target != null) {
            if (!player.hasPermission("fundamentals.command.heal.other")) {
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

        target.setHealth(target.getMaxHealth());

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getMessages().getComponent("commands.heal.self"));
            return;
        }

        target.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.heal.healed-by-admin", Placeholder.unparsed("administrator", player.getName())
                )
        );

        player.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.heal.other", Placeholder.unparsed("target", target.getName())
                )
        );
    }
}
