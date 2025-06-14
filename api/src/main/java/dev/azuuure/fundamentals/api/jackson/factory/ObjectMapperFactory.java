package dev.azuuure.fundamentals.api.jackson.factory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.azuuure.fundamentals.api.jackson.module.BukkitJacksonModule;

public class ObjectMapperFactory {

    public static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.registerModule(new BukkitJacksonModule());
        return mapper;
    }
}
