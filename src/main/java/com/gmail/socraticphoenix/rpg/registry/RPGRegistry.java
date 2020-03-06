package com.gmail.socraticphoenix.rpg.registry;

import java.util.Collection;
import java.util.Optional;

public interface RPGRegistry<T extends RPGRegistryItem> extends RPGRegistryItem {

    void register(T value);

    default void register(T... values) {
        for (T val : values) {
            register(val);
        }
    }

    Optional<T> get(String id);

    Collection<T> elements();

    Class<T> type();

}
