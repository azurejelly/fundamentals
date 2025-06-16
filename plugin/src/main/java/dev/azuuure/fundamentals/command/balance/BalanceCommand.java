package dev.azuuure.fundamentals.command.balance;

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

@Command(value = "balance", alias = { "bal", "fbalance", "fbal", "money", "fmoney", "coins", "fcoins" })
public class BalanceCommand extends BaseCommand {

    private final Fundamentals plugin;

    public BalanceCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.balance")
    public void execute(CommandSender sender, @Optional Player target) {
        Player player = (sender instanceof Player)
                ? (Player) sender
                : null;

        if (player != null) {
            if (target == null) {
                target = player;
            } else {
                if (!player.hasPermission("fundamentals.command.balance.other")) {
                    sender.sendMessage(plugin.getMessages().getComponent("commands.general.no-permission"));
                    return;
                }
            }
        } else {
            if (target == null) {
                sender.sendMessage(plugin.getMessages().getComponent("commands.general.must-provide-player"));
                return;
            }
        }

        User user = plugin.getUserStorage().getUser(target.getUniqueId());
        if (user == null) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.general.user-not-found"));
            return;
        }

        String formatted = String.format("%.2f", user.getMoney());
        if (player != null && player.getUniqueId().equals(target.getUniqueId())) {
            sender.sendMessage(
                    plugin.getMessages().getComponent("commands.balance.self",
                            Placeholder.unparsed("balance", formatted)
                    )
            );
        } else {
            sender.sendMessage(
                    plugin.getMessages().getComponent("commands.balance.other",
                            Placeholder.unparsed("target", target.getName()),
                            Placeholder.unparsed("balance", formatted)
                    )
            );
        }
    }
}
