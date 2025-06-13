package dev.azuuure.fundamentals.api.loader;

public interface Loader {

    void load();

    default void unload() {}
}