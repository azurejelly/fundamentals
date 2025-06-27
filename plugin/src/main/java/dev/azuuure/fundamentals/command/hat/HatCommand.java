package dev.azuuure.fundamentals.command.hat;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "hat", alias = { "fhat", "head", "fhead" })
public class HatCommand extends BaseCommand {

    @Default
    public void execute(CommandSender sender, @Optional Player target) {
        // ...
    }
}
