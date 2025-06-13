package dev.azuuure.fundamentals.api.user;

import dev.azuuure.fundamentals.api.util.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class User {

    private final UUID uuid;
    private String lastKnownName;
    private Location lastKnownLocation;
    private String lastAddress;
    private final long firstSeen;
    private long lastSeen;

    public User() {
        this.uuid = UUID.randomUUID();
        this.lastKnownName = null;
        this.lastKnownLocation = null;
        this.lastAddress = null;
        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
    }

    public User(Player player) {
        this.uuid = player.getUniqueId();
        this.lastKnownName = player.getName();
        this.lastKnownLocation = player.getLocation();
        this.lastAddress = player.getAddress() != null ? player.getAddress().getHostString() : "n/a";
        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
    }

    public User(UUID uuid) {
        this.uuid = uuid;
        this.lastKnownName = "Unknown";
        this.lastKnownLocation = null;
        this.lastAddress = null;
        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
    }

    public User(
            UUID uuid,
            String lastKnownName,
            Location lastKnownLocation,
            String lastAddress,
            long firstSeen,
            long lastSeen
    ) {
        this.uuid = uuid;
        this.lastKnownName = lastKnownName;
        this.lastKnownLocation = lastKnownLocation;
        this.lastAddress = lastAddress;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

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
            setLastAddress(player.getAddress() != null ? player.getAddress().getHostString() : "n/a");
            updateLastSeen();
        }
    }

    public Map<String, Object> serialize() {
        return Map.of(
                "uuid", uuid.toString(),
                "lastKnownName", lastKnownName,
                "lastKnownLocation", lastKnownLocation != null
                        ? LocationUtils.serialize(lastKnownLocation)
                        : "",
                "lastAddress", lastAddress,
                "firstSeen", firstSeen,
                "lastSeen", lastSeen
        );
    }
}
