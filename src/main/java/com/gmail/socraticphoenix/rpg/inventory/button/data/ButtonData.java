package com.gmail.socraticphoenix.rpg.inventory.button.data;

import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.impl.ButtonDataImpl;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface ButtonData extends DataManipulator<ButtonData, ImmutableButtonData> {

    static ButtonData of(ButtonAction action) {
        return new ButtonDataImpl(action);
    }

    Value<ButtonAction> action();

}
