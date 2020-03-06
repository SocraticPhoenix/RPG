package com.gmail.socraticphoenix.rpg.click.data.impl;

import com.gmail.socraticphoenix.collect.Items;
import com.gmail.socraticphoenix.rpg.RPGDataKeys;
import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.data.ImmutablePlayerClickData;
import com.gmail.socraticphoenix.rpg.click.data.PlayerClickData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerClickDataManipulatorBuilder implements DataManipulatorBuilder<PlayerClickData, ImmutablePlayerClickData> {

    @Override
    public PlayerClickData create() {
        return new PlayerClickDataImpl(Items.buildList(), 0);
    }

    @Override
    public Optional<PlayerClickData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    public Optional<PlayerClickData> build(DataView container) throws InvalidDataException {
        if (!container.contains(RPGDataKeys.LAST_CLICKED, RPGDataKeys.CLICK_SEQUENCE)) {
            return Optional.empty();
        }

        return Optional.of(new PlayerClickDataImpl(container.getStringList(RPGDataKeys.CLICK_SEQUENCE.getQuery()).get().stream().map(ClickType::valueOf).collect(Collectors.toList()),
                container.getLong(RPGDataKeys.LAST_CLICKED.getQuery()).get()));
    }

}
