package dev.azuuure.fundamentals.api.storage.type;

public enum StorageType {

    YAML("YAML"),
    JSON("JSON"),
    SQLITE("SQLite"),
    MYSQL("MySQL"),
    MONGODB("MongoDB");

    private final String readableName;

    StorageType(String readableName) {
        this.readableName = readableName;
    }

    @Override
    public String toString() {
        return readableName;
    }
}
