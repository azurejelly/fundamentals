package dev.azuuure.fundamentals.utils;

import org.bukkit.Bukkit;

public class VaultUtils {

    public static boolean isVaultAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }
}
