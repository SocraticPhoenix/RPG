package com.gmail.socraticphoenix.rpg.data.sponge.impl;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGCustomData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGImmutableCustomData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;
import java.util.function.Function;

public class RPGCustomDataBuilder<T extends RPGData<T>, M extends RPGCustomData<T, M, I>, I extends RPGImmutableCustomData<T, M, I>> implements DataManipulatorBuilder<M, I> {
    private Key<Value<T>> key;
    private T defaultValue;
    private Class<T> type;
    private Function<T, M> constructor;

    public RPGCustomDataBuilder(Key<Value<T>> key, T defaultValue, Class<T> type, Function<T, M> constructor) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
        this.constructor = constructor;
    }

    @Override
    public M create() {
        return this.constructor.apply(this.defaultValue.copy());
    }

    @Override
    public Optional<M> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    public Optional<M> build(DataView container) throws InvalidDataException {
        if (!container.contains(this.key)) {
            return Optional.empty();
        }

        return Optional.of(this.constructor.apply(container.getSerializable(this.key.getQuery(), this.type).get()));
    }

}
