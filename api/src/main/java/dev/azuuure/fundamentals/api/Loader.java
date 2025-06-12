package dev.azuuure.fundamentals.api;

public interface Loader {

    void load();

    default void unload() {}
}