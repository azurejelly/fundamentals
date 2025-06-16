package dev.azuuure.fundamentals.api.storage.exception;

public class StorageShutdownException extends RuntimeException {

    public StorageShutdownException(String message) {
        super(message);
    }

    public StorageShutdownException(String message, Throwable cause) {
        super(message, cause);
    }
}
