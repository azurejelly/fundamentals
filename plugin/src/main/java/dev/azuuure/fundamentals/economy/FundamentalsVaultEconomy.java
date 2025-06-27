package dev.azuuure.fundamentals.economy;

import dev.azuuure.fundamentals.Fundamentals;
import dev.azuuure.fundamentals.api.user.User;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class FundamentalsVaultEconomy implements Economy {

    private final Fundamentals plugin;

    public FundamentalsVaultEconomy(Fundamentals plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("config.vault.economy.enabled", true);
    }

    @Override
    public String getName() {
        return "fundamentals-eco";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return String.format(plugin.getConfig().getString("config.vault.economy.format", "%.2f"), v);
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("config.vault.currency-name.plural", "dollars");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("config.vault.currency-name.singular", "dollar");
    }

    @Override
    public boolean hasAccount(String name) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(name);
        return hasAccount(offlinePlayer);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            return false;
        }

        return plugin.getUserStorage().exists(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean hasAccount(String name, String world) {
        return false; // unsupported
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false; // unsupported
    }

    @Override
    public double getBalance(String name) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(name);
        return getBalance(offlinePlayer);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        User user = plugin.getUserStorage().getUser(offlinePlayer.getUniqueId());
        if (user == null) {
            return 0;
        }

        return user.getMoney();
    }

    @Override
    public double getBalance(String name, String world) {
        return 0; // unsupported
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return 0; // unsupported
    }

    @Override
    public boolean has(String name, double value) {
        return getBalance(name) >= value;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double value) {
        return getBalance(offlinePlayer) >= value;
    }

    @Override
    public boolean has(String name, String world, double value) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String world, double value) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double value) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(name);
        return withdrawPlayer(offlinePlayer, value);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double value) {
        if (offlinePlayer == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                    "Player not found.");
        }

        User user = plugin.getUserStorage().getUser(offlinePlayer.getUniqueId());
        if (user == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                    "User not found.");
        }

        if (user.getMoney() < value) {
            return new EconomyResponse(0, user.getMoney(), EconomyResponse.ResponseType.FAILURE,
                    "Insufficient funds.");
        }

        user.setMoney(user.getMoney() - value);
        return new EconomyResponse(value, user.getMoney(), EconomyResponse.ResponseType.SUCCESS,
                "Withdrawn " + format(value) + " from " + offlinePlayer.getName() + ".");
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double value) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Per-world economy is not supported.");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double value) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Per-world economy is not supported.");
    }

    @Override
    public EconomyResponse depositPlayer(String name, double value) {
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(name);
        return depositPlayer(offlinePlayer, value);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        if (offlinePlayer == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                    "Player not found.");
        }

        User user = plugin.getUserStorage().getUser(offlinePlayer.getUniqueId());
        if (user == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                    "User not found.");
        }

        user.setMoney(user.getMoney() + v);
        return new EconomyResponse(v, user.getMoney(), EconomyResponse.ResponseType.SUCCESS,
                "Deposited " + format(v) + " to " + offlinePlayer.getName() + ".");
    }

    @Override
    public EconomyResponse depositPlayer(String name, String world, double value) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Per-world economy is not supported.");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double value) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Per-world economy is not supported.");
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Banks are not supported.");
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}
