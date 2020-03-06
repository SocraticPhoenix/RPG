package com.gmail.socraticphoenix.rpg.click.data;

import com.gmail.socraticphoenix.rpg.click.ClickType;
import com.gmail.socraticphoenix.rpg.click.data.impl.PlayerClickDataImpl;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.List;
import java.util.UUID;

public interface PlayerClickData extends DataManipulator<PlayerClickData, ImmutablePlayerClickData> {

    static PlayerClickData of(List<ClickType> sequence, long lastClicked) {
        return new PlayerClickDataImpl(sequence, lastClicked);
    }

    Value<Long> lastClicked();

    ListValue<ClickType> sequence();

}
