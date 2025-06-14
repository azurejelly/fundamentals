package dev.azuuure.fundamentals.api.storage.type;

public enum StorageType {

    FILE("JSON/File"),
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
