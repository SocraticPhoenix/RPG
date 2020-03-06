package com.gmail.socraticphoenix.rpg.data.sponge.impl;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGCustomData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGImmutableCustomData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

public abstract class RPGImmutableCustomDataImpl<T extends RPGData<T>, M extends RPGCustomData<T, M, I>, I extends RPGImmutableCustomData<T, M, I>> extends AbstractImmutableData<I, M> implements RPGImmutableCustomData<T, M, I> {
    protected T value;
    private Key<Value<T>> key;

    public RPGImmutableCustomDataImpl(T value, Key<Value<T>> key) {
        this.value = value;
        this.key = key;
    }

    @Override
    public ImmutableValue<T> value() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(this.key, this.value).asImmutable();
    }

    public T getValue() {
        return value;
    }

    @Override
    protected void registerGetters() {
        this.registerKeyValue(this.key, this::value);
        this.registerFieldGetter(this.key, this::getValue);
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(this.key, this.value);
    }

    @Override
    public abstract M asMutable();

    @Override
    public abstract int getContentVersion();

}
