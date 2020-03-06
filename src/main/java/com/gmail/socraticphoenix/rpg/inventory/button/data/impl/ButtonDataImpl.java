package com.gmail.socraticphoenix.rpg.inventory.button.data.impl;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ImmutableButtonData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class ButtonDataImpl extends AbstractSingleData<ButtonAction, ButtonData, ImmutableButtonData> implements ButtonData {

    public ButtonDataImpl(ButtonAction value) {
        super(value, RPGDataKeys.BUTTON_ACTION);
    }

    @Override
    protected Value<ButtonAction> getValueGetter() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.BUTTON_ACTION, this.getValue());
    }

    @Override
    public Value<ButtonAction> action() {
        return getValueGetter();
    }

    @Override
    public Optional<ButtonData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ButtonData> mergeData = dataHolder.get(ButtonData.class);

        if (mergeData.isPresent()) {
            ButtonData merged = overlap.merge(this, mergeData.get());
            this.setValue(merged.action().get());
        }

        return Optional.of(this);
    }

    @Override
    public Optional<ButtonData> from(DataContainer container) {
        if (!container.contains(RPGDataKeys.BUTTON_ACTION)) {
            return Optional.empty();
        }

        this.setValue(container.getSerializable(RPGDataKeys.BUTTON_ACTION.getQuery(), ButtonAction.class).get());

        return Optional.of(this);
    }

    @Override
    public DataContainer toContainer() {
        return fillContainer(super.toContainer());
    }

    protected DataContainer fillContainer(DataContainer container) {
        container.set(RPGDataKeys.BUTTON_ACTION, this.getValue());

        return container;
    }

    @Override
    public ButtonData copy() {
        return new ButtonDataImpl(this.getValue());
    }

    @Override
    public ImmutableButtonData asImmutable() {
        return new ImmutableButtonDataImpl(this.getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

}
