package com.gmail.socraticphoenix.rpg.data;

import org.spongepowered.api.entity.living.player.Player;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface RPGDataStorage {

    void init() throws IOException;

    Optional<RPGPlayerData> get(UUID player);

    default Optional<RPGPlayerData> get(Player player) {
        return get(player.getUniqueId());
    }

    RPGPlayerData getOrCreate(UUID player, Consumer<RPGPlayerData> consumer);

    default RPGPlayerData getOrCreate(UUID player) {
        return getOrCreate(player, d -> {});
    }

    default RPGPlayerData getOrCreate(Player player, Consumer<RPGPlayerData> consumer) {
        return getOrCreate(player.getUniqueId(), consumer);
    }

    default RPGPlayerData getOrCreate(Player player) {
        return getOrCreate(player.getUniqueId());
    }

    void save(UUID player);

    default void save(Player player) {
        save(player.getUniqueId());
    }

    void saveAndRemove(UUID id);

    default void saveAndRemove(Player player) {
        saveAndRemove(player.getUniqueId());
    }

    void remove(UUID player);

    void saveAll();

    default void remove(Player player) {
        remove(player.getUniqueId());
    }

}
