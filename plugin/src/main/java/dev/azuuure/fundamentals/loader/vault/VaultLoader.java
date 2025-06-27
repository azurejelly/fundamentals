package dev.azuuure.fundamentals.loader.vault;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.loader.Loader;
import dev.azuuure.fundamentals.economy.FundamentalsVaultEconomy;
import dev.azuuure.fundamentals.utils.VaultUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VaultLoader implements Loader {

    private final Fundamentals plugin;

    public VaultLoader(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        if (!VaultUtils.isVaultAvailable()) {
            plugin.getLogger().warning("Vault not found, economy-related features will be disabled.");
            return;
        }

        if (plugin.getConfig().getBoolean("config.vault.economy.enabled", true)) {
            Economy economy = new FundamentalsVaultEconomy(plugin);
            plugin.getServer()
                    .getServicesManager()
                    .register(Economy.class, economy, plugin, ServicePriority.Highest);

            plugin.getLogger().info("Vault integration enabled - economy-related features are now active.");
        }
    }
}
