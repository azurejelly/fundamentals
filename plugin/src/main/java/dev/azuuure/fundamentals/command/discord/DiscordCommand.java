package dev.azuuure.fundamentals.command.discord;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.util.ValidationUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.logging.Level;

@Command(value = "discord", alias = { "fdiscord" })
public class DiscordCommand extends BaseCommand {

    private final Fundamentals plugin;

    public DiscordCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.discord")
    public void execute(CommandSender sender) {
        String invite = plugin.getConfig().getString("config.social-media.discord", "");
        if (invite.isEmpty()) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.discord.no-invite"));
            return;
        }

        sender.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.discord.invite", Placeholder.parsed("invite", invite)
                )
        );
    }

    @SubCommand("set")
    @Permission("fundamentals.command.discord.set")
    public void execute(CommandSender sender, String invite) {
        if (!ValidationUtils.isDiscordInvite(invite)) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.discord.set.invalid-invite"));
            return;
        }

        try {
            plugin.getConfig().set("config.social-media.discord", invite);
            plugin.getConfig().save();
        } catch (IOException e) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.discord.set.save-fail"));
            plugin.getLogger().log(Level.SEVERE, "Failed to save Discord invite", e);
            return;
        }

        sender.sendMessage(
                plugin.getMessages().getComponent("commands.discord.set.success",
                        Placeholder.parsed("invite", invite)
                )
        );
    }
}
