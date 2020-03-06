package com.gmail.socraticphoenix.rpg.click.data;

import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.data.impl.ImmutableItemClickDataImpl;
import com.gmail.socraticphoenix.rpg.click.data.impl.ImmutablePlayerClickDataImpl;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.List;
import java.util.UUID;

public interface ImmutablePlayerClickData extends ImmutableDataManipulator<ImmutablePlayerClickData, PlayerClickData> {

    static ImmutablePlayerClickData of(List<ClickType> sequence, long lastClicked) {
        return new ImmutablePlayerClickDataImpl(sequence, lastClicked);
    }

    ImmutableValue<Long> lastClicked();

    ImmutableListValue<ClickType> sequence();

}
