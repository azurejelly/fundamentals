package dev.azuuure.fundamentals.api.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

public class LocationDeserializer extends JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        String worldName = node.get("world").asText();
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IOException("Could not find world " + worldName);
        }

        double x = node.get("x").asDouble();
        double y = node.get("y").asDouble();
        double z = node.get("z").asDouble();
        float yaw = (float) node.get("yaw").asDouble();
        float pitch = (float) node.get("pitch").asDouble();
        return new Location(world, x, y, z, yaw, pitch);
    }
}
