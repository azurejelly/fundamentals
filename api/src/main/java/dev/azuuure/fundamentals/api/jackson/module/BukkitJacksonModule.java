package dev.azuuure.fundamentals.api.jackson.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.azuuure.fundamentals.api.jackson.deserializer.LocationDeserializer;
import dev.azuuure.fundamentals.api.jackson.serializer.LocationSerializer;
import org.bukkit.Location;

public class BukkitJacksonModule extends SimpleModule {

    public BukkitJacksonModule() {
        addSerializer(Location.class, new LocationSerializer());
        addDeserializer(Location.class, new LocationDeserializer());
    }
}
