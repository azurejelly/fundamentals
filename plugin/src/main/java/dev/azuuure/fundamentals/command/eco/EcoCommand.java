package dev.azuuure.fundamentals.command.eco;

import dev.azuuure.fundamentals.Fundamentals;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

@Command(value = "eco", alias = { "economy" })
public class EcoCommand extends BaseCommand {

    private final Fundamentals plugin;

    public EcoCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.eco")
    public void execute(CommandSender sender, Player target, String action, @Optional Double amount) {
        RegisteredServiceProvider<Economy> provider = plugin.getServer()
                .getServicesManager()
                .getRegistration(Economy.class);

        if (provider == null) {
            sender.sendMessage(plugin.getMessages().getComponent("errors.no-economy-implementation"));
            return;
        }

        Economy economy = provider.getProvider();
        switch (action.toLowerCase()) {
            case "add": {
                if (amount == null) {
                    sender.sendMessage(plugin.getMessages().getComponent("commands.eco.no-amount"));
                    return;
                }

                economy.depositPlayer(target, amount);
                sender.sendMessage(
                        plugin.getMessages().getComponent("commands.eco.add.success",
                                Placeholder.unparsed("amount", economy.format(amount)),
                                Placeholder.unparsed("new_amount", economy.format(economy.getBalance(target))),
                                Placeholder.unparsed("target", target.getName())
                        )
                );

                return;
            }
            case "remove": {
                if (amount == null) {
                    sender.sendMessage(plugin.getMessages().getComponent("commands.eco.no-amount"));
                    return;
                }

                economy.withdrawPlayer(target, amount);
                sender.sendMessage(
                        plugin.getMessages().getComponent("commands.eco.remove.success",
                                Placeholder.unparsed("amount", economy.format(amount)),
                                Placeholder.unparsed("new_amount", economy.format(economy.getBalance(target))),
                                Placeholder.unparsed("target", target.getName())
                        )
                );

                return;
            }
            case "set": {
                if (amount == null) {
                    sender.sendMessage(plugin.getMessages().getComponent("commands.eco.no-amount"));
                    return;
                }

                economy.withdrawPlayer(target, economy.getBalance(target));
                economy.depositPlayer(target, amount);

                sender.sendMessage(
                        plugin.getMessages().getComponent("commands.eco.set.success",
                                Placeholder.unparsed("amount", economy.format(amount)),
                                Placeholder.unparsed("target", target.getName())
                        )
                );

                return;
            }
            case "clear":
            case "reset": {
                economy.withdrawPlayer(target, economy.getBalance(target));
                sender.sendMessage(
                        plugin.getMessages().getComponent("commands.eco.set.success",
                                Placeholder.unparsed("amount", economy.format(amount)),
                                Placeholder.unparsed("target", target.getName())
                        )
                );

                return;
            }
            default: {
                sender.sendMessage(plugin.getMessages().getComponent("commands.eco.invalid-op"));
            }
        }
    }
}
