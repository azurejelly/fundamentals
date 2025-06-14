package dev.azuuure.fundamentals.command.teleport;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "tp", alias = { "ftp", "teleport", "fteleport" })
public class TeleportCommand extends BaseCommand {

    @Default
    @Permission("fundamentals.command.teleport")
    public void execute(CommandSender sender, Player player, @Optional Player target) {
        // ...
    }
}
