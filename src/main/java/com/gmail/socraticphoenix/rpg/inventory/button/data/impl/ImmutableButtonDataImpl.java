package com.gmail.socraticphoenix.rpg.inventory.button.data.impl;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ImmutableButtonData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableButtonDataImpl extends AbstractImmutableSingleData<ButtonAction, ImmutableButtonData, ButtonData> implements ImmutableButtonData {

    public ImmutableButtonDataImpl(ButtonAction value) {
        super(value, RPGDataKeys.BUTTON_ACTION);
    }

    @Override
    protected ImmutableValue<ButtonAction> getValueGetter() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(RPGDataKeys.BUTTON_ACTION, this.getValue()).asImmutable();
    }

    @Override
    public ImmutableValue<ButtonAction> action() {
        return getValueGetter();
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
    public ButtonData asMutable() {
        return new ButtonDataImpl(this.getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }


}
