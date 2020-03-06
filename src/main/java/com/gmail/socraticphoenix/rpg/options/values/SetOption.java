package com.gmail.socraticphoenix.rpg.options.values;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public abstract class SetOption<T> implements DataSerializable {
    protected T value;
    protected Option<T> type;

    public SetOption(T value, Option<T> type) {
        this.value = value;
        this.type = type;
    }

    public void setValue(T val) {
        this.value = val;
    }

    public T getValue() {
        return this.value;
    }

    public Option<T> type() {
        return this.type;
    }

    public abstract SetOption<T> copy();

    public abstract void fill(DataContainer container);

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew()
                .set(RPGData.TYPE, this.type);
        fill(container);
        return container;
    }

    public static class Builder implements DataBuilder<SetOption> {

        @Override
        public Optional<SetOption> build(DataView container) throws InvalidDataException {
            if (!container.contains(RPGData.OPTION, RPGData.TYPE)) {
                return Optional.empty();
            }

            Option type = container.getSerializable(RPGData.TYPE, Option.class).get();
            return Optional.ofNullable(type.build(container.getContainer()));
        }

    }

}
