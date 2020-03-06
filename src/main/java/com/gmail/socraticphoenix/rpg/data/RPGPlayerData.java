package com.gmail.socraticphoenix.rpg.data;

import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RPGPlayerData {
    private UUID player;
    private boolean initialized;
    private List<Consumer<RPGPlayerData>> initializationListeners;

    private PlayerData playerData;
    private List<CharacterData> characters;

    public Optional<CharacterData> activeCharacter() {
        for (CharacterData data : characters) {
            if (data.isActive()) {
                return Optional.of(data);
            }
        }

        return Optional.empty();
    }

    public void active(Consumer<CharacterData> action) {
        activeCharacter().ifPresent(action);
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void setCharacters(List<CharacterData> characters) {
        this.characters = characters;
    }

    public RPGPlayerData(UUID player, PlayerData playerData, List<CharacterData> characters) {
        this.player = player;
        this.initialized = false;
        this.initializationListeners = new ArrayList<>();
        this.playerData = playerData;
        this.characters = characters;
    }

    public RPGPlayerData copy() {
        return new RPGPlayerData(player, playerData.copy(), characters.stream().map(CharacterData::copy).collect(Collectors.toList()));
    }

    public List<CharacterData> getCharacters() {
        return characters;
    }

    public RPGPlayerData addInitializationListener(Consumer<RPGPlayerData> consumer) {
        this.initializationListeners.add(consumer);
        return this;
    }

    public void initialize() {
        if (!initialized) {
            this.initialized = true;
            Sponge.getScheduler().createTaskBuilder()
                    .execute(() -> {
                        this.initializationListeners.forEach(c -> c.accept(this));
                        this.initializationListeners.clear();
                    })
                    .submit(RPGPlugin.getPlugin());
        }
    }

}
