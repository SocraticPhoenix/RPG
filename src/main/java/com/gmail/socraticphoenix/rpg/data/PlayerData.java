package com.gmail.socraticphoenix.rpg.data;

import com.gmail.socraticphoenix.rpg.data.character.CharacterData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class PlayerData extends RPGData<PlayerData> {

    public PlayerData() {
        super(0);
    }

    @Override
    public PlayerData copy() {
        return new PlayerData();
    }

    @Override
    public DataContainer fill(DataContainer container) {
        return container;
    }

    @Override
    public Optional<PlayerData> from(DataView container) {
        return Optional.of(new PlayerData());
    }

    public static class Builder implements DataBuilder<PlayerData> {

        @Override
        public Optional<PlayerData> build(DataView container) throws InvalidDataException {
            return new PlayerData().from(container);
        }

    }

}
