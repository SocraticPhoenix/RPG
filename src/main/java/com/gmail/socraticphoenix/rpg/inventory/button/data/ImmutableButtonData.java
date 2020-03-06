package com.gmail.socraticphoenix.rpg.inventory.button.data;

import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.impl.ImmutableButtonDataImpl;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableButtonData extends ImmutableDataManipulator<ImmutableButtonData, ButtonData> {

    static ImmutableButtonData of(ButtonAction action) {
        return new ImmutableButtonDataImpl(action);
    }

    ImmutableValue<ButtonAction> action();

}
