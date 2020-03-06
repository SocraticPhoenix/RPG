package com.gmail.socraticphoenix.rpg.data.sponge.impl;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGCustomData;
import com.gmail.socraticphoenix.rpg.data.sponge.RPGImmutableCustomData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public abstract class RPGCustomDataImpl<T extends RPGData<T>, M extends RPGCustomData<T, M, I>, I extends RPGImmutableCustomData<T, M, I>> extends AbstractData<M, I> implements RPGCustomData<T, M, I> {
    protected T value;
    private Class<T> type;
    private Key<Value<T>> key;
    private Class<? extends M> impl;

    public RPGCustomDataImpl(T value, Key<Value<T>> key, Class<T> type, Class<? extends M> impl) {
        this.value = value;
        this.key = key;
        this.impl = impl;
        this.type= type;

        this.registerGettersAndSetters();
    }

    @Override
    public Value<T> value() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(this.key, this.value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Key<Value<T>> getKey() {
        return key;
    }

    @Override
    protected void registerGettersAndSetters() {
        this.registerKeyValue(this.key, this::value);
        this.registerFieldGetter(this.key, this::getValue);
        this.registerFieldSetter(this.key, this::setValue);
    }

    @Override
    public Optional<M> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<? extends M> mergeData = dataHolder.get(this.impl);

        if (mergeData.isPresent()) {
            RPGCustomData<T, M, I> merge = overlap.merge(this, mergeData.get());

            this.value = merge.value().get();
        }

        return (Optional<M>) Optional.of(this);
    }

    @Override
    public Optional<M> from(DataContainer container) {
        if (!container.contains(this.key)) {
            return Optional.empty();
        }

        this.value = container.getSerializable(this.key.getQuery(), this.type).get();

        return (Optional<M>) Optional.of(this);
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(this.key, this.value);
    }

    @Override
    public abstract M copy();

    @Override
    public abstract I asImmutable();

    @Override
    public abstract int getContentVersion();

}
