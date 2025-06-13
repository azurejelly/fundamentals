package dev.azuuure.fundamentals.command.website;

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

@Command(value = "website", alias = { "fwebsite" })
@Permission("fundamentals.command.website")
public class WebsiteCommand extends BaseCommand {

    private final Fundamentals plugin;

    public WebsiteCommand(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("fundamentals.command.website")
    public void execute(CommandSender sender) {
        String invite = plugin.getConfig().getString("config.social-media.website", "");
        if (invite.isEmpty()) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.website.no-website"));
            return;
        }

        sender.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.website.link", Placeholder.parsed("url", invite)
                )
        );
    }


    @SubCommand("set")
    @Permission("fundamentals.command.website.set")
    public void execute(CommandSender sender, String invite) {
        if (!ValidationUtils.isValidURL(invite)) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.website.set.invalid-url"));
            return;
        }

        try {
            plugin.getConfig().set("config.social-media.website", invite);
            plugin.getConfig().save();
        } catch (IOException e) {
            sender.sendMessage(plugin.getMessages().getComponent("commands.website.set.save-fail"));
            plugin.getLogger().log(Level.SEVERE, "Failed to save website URL", e);
            return;
        }

        sender.sendMessage(
                plugin.getMessages().getComponent(
                        "commands.website.set.success", Placeholder.parsed("url", invite)
                )
        );
    }
}
