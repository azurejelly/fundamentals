package dev.azuuure.fundamentals.api.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class User {

    private final UUID uuid;
    private String lastKnownName;
    private Location lastKnownLocation;
    private String lastAddress;
    private final long firstSeen;
    private long lastSeen;
    private boolean allowsTeleport;

    public User() {
        this(
                UUID.randomUUID(),
                null,
                null,
                null,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                true
        );
    }

    @JsonCreator
    public User(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("lastKnownName") String lastKnownName,
            @JsonProperty("lastKnownLocation") Location lastKnownLocation,
            @JsonProperty("lastAddress") String lastAddress,
            @JsonProperty("firstSeen") long firstSeen,
            @JsonProperty("lastSeen") long lastSeen,
            @JsonProperty("allowsTeleport") boolean allowsTeleport
    ) {
        this.uuid = uuid;
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

    public boolean isAllowsTeleport() {
        return allowsTeleport;
    }

    public void setAllowsTeleport(boolean allowsTeleport) {
        this.allowsTeleport = allowsTeleport;
    }

    public static User fromPlayer(Player player) {
        return new User(
                player.getUniqueId(),
                player.getName(),
                player.getLocation(),
                player.getAddress() != null
                        ? player.getAddress().getHostString()
                        : null,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                true
        );
    }

    public static User fromUUID(UUID uuid) {
        return new User(
                uuid,
                null,
                null,
                null,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                true
        );
    }
}
