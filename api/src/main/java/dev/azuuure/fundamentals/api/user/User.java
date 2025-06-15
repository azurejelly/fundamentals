package dev.azuuure.fundamentals.api.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mongojack.Id;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;

public final class User {

    @Id @JsonProperty("_id")
    private final UUID uuid;
    private double money;
    private String lastKnownName;
    private Location lastKnownLocation;
    private String lastAddress;
    private final long firstSeen;
    private long lastSeen;
    private boolean allowsTeleport;

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.lastKnownName = name;
        this.lastKnownLocation = null;
        this.lastAddress = null;
        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
        this.money = 0;
        this.allowsTeleport = true;
    }

    @JsonCreator
    public User(
            @Id @JsonProperty("_id") UUID uuid,
            @JsonProperty("money") double money,
            @JsonProperty("lastKnownName") String lastKnownName,
            @JsonProperty("lastKnownLocation") Location lastKnownLocation,
            @JsonProperty("lastAddress") String lastAddress,
            @JsonProperty("firstSeen") long firstSeen,
            @JsonProperty("lastSeen") long lastSeen,
            @JsonProperty("allowsTeleport") boolean allowsTeleport
    ) {
        this.uuid = uuid;
        this.money = money;
        this.lastKnownName = lastKnownName;
        this.lastKnownLocation = lastKnownLocation;
        this.lastAddress = lastAddress;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
        this.allowsTeleport = allowsTeleport;
    }

    public UUID getUUID() {
        return uuid;
    }

    @JsonIgnore
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @JsonIgnore
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public String getLastKnownName() {
        return lastKnownName;
    }

    public void setLastKnownName(String lastKnownName) {
        this.lastKnownName = lastKnownName;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    public void setLastAddress(InetAddress address) {
        this.lastAddress = address != null
                ? address.getHostAddress()
                : null;
    }

    public void setLastAddress(InetSocketAddress socketAddress) {
        if (socketAddress == null) {
            this.lastAddress = null;
            return;
        }

        this.setLastAddress(socketAddress.getAddress());
    }

    public long getFirstSeen() {
        return firstSeen;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void updateLastSeen() {
        this.lastSeen = System.currentTimeMillis();
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void refresh() {
        Player player = getPlayer();
        if (player != null) {
            setLastKnownName(player.getName());
            setLastKnownLocation(player.getLocation());
            setLastAddress(player.getAddress());
            updateLastSeen();
        }
    }

    public boolean isAllowsTeleport() {
        return allowsTeleport;
    }

    public void setAllowsTeleport(boolean allowsTeleport) {
        this.allowsTeleport = allowsTeleport;
    }

    public double getMoney() {
        return money;
    }

    public void addMoney(double amount) {
        this.money += amount;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void removeMoney(double amount) {
        this.money -= amount;
    }

    public static User from(UUID uuid, String name, InetAddress address) {
        String ip = address != null
                ? address.getHostAddress()
                : null;

        return new User(
                uuid,
                0,
                name,
                null,
                ip,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                true
        );
    }
}
