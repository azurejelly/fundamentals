package dev.azuuure.fundamentals.api.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class LocationUtils {

    private LocationUtils() {
        throw new UnsupportedOperationException();
    }

    public static String serialize(Location loc) {
        if (loc == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        return loc.getWorld().getName()
                + ":" + loc.getX()
                + ":" + loc.getY()
                + ":" + loc.getZ()
                + ":" + loc.getYaw()
                + ":" + loc.getPitch();
    }

    public static Location deserialize(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Serialized location string cannot be null or empty");
        }

        String[] parts = s.split(":");
        if (parts.length != 6) {
            throw new IllegalArgumentException(
                    "Invalid serialized location string format. Expected format: world:x:y:z:yaw:pitch"
            );
        }

        World world = Bukkit.getServer().getWorld(parts[0]);
        if (world == null) {
            throw new IllegalArgumentException("World '" + parts[0] + "' not found");
        }

        try {
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid number in serialized location string", ex);
        }
    }
}
