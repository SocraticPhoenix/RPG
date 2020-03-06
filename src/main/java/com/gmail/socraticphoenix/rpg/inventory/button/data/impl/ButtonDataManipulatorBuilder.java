package com.gmail.socraticphoenix.rpg.inventory.button.data.impl;

import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.RPGPlugin;
import com.gmail.socraticphoenix.rpg.inventory.InventoryHelper;
import com.gmail.socraticphoenix.rpg.inventory.button.ButtonAction;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ButtonData;
import com.gmail.socraticphoenix.rpg.inventory.button.data.ImmutableButtonData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class ButtonDataManipulatorBuilder implements DataManipulatorBuilder<ButtonData, ImmutableButtonData> {
    private RPGPlugin plugin;

    public ButtonDataManipulatorBuilder(RPGPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ButtonData create() {
        return InventoryHelper.createNoopButton();
    }

    @Override
    public Optional<ButtonData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    public Optional<ButtonData> build(DataView container) throws InvalidDataException {
        if (!container.contains(RPGDataKeys.BUTTON_ACTION)) {
            return Optional.empty();
        }

        return Optional.of(new ButtonDataImpl(container.getSerializable(RPGDataKeys.BUTTON_ACTION.getQuery(), ButtonAction.class).get()));
    }

}
