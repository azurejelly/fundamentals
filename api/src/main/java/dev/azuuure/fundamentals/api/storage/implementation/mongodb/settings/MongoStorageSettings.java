package dev.azuuure.fundamentals.api.storage.implementation.mongodb.settings;

public final class MongoStorageSettings {

    private final String uri;
    private final String hostname;
    private final int port;
    private final String username;
    private final char[] password;
    private final String authDatabase;
    private final String database;

    public MongoStorageSettings(String uri, String hostname, int port, String username, char[] password, String authDatabase, String database) {
        this.uri = uri;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.authDatabase = authDatabase;
        this.database = database;
    }

    public MongoStorageSettings(String uri, String database) {
        this(uri, null, 27017, null, null, "admin", database);
    }

    public MongoStorageSettings(String hostname, int port, String username, char[] password, String authDatabase, String database) {
        this(null, hostname, port, username, password, authDatabase, database);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getURI() {
        return uri;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public String getAuthDatabase() {
        return authDatabase;
    }

    public static class Builder {

        private String uri;
        private String hostname;
        private int port;
        private String username;
        private char[] password;
        private String authDatabase;
        private String database;

        private Builder() {
            this.hostname = "127.0.0.1";
            this.port = 27017;
            this.database = "fundamentals";
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(char[] password) {
            this.password = password;
            return this;
        }

        public Builder password(String password) {
            this.password = password != null
                    ? password.toCharArray()
                    : null;

            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder authDatabase(String authDatabase) {
            this.authDatabase = authDatabase;
            return this;
        }

        public MongoStorageSettings build() {
            if (database == null || database.isEmpty()) {
                throw new IllegalArgumentException("Database name cannot be null or empty.");
            }

            if (authDatabase == null || authDatabase.isEmpty()) {
                throw new IllegalArgumentException("Authentication database cannot be null or empty.");
            }

            if (uri == null) {
                if (hostname == null || hostname.isEmpty()) {
                    throw new IllegalArgumentException("Hostname cannot be null or empty when URI is not provided.");
                }

                if (port <= 0 || port > 65535) {
                    throw new IllegalArgumentException("Port must be between 1 and 65535.");
                }

                return new MongoStorageSettings(hostname, port, username, password, authDatabase, database);
            } else {
                return new MongoStorageSettings(uri, database);
            }
        }
    }
}
