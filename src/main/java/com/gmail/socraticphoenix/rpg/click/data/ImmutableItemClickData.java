package com.gmail.socraticphoenix.rpg.click.data;

import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.click.data.impl.ImmutableItemClickDataImpl;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface ImmutableItemClickData extends ImmutableDataManipulator<ImmutableItemClickData, ItemClickData> {

    static ImmutableItemClickData of(ItemClickPredicate predicate, long interval) {
        return new ImmutableItemClickDataImpl(predicate, interval);
    }

    ImmutableValue<ItemClickPredicate> predicate();

    ImmutableValue<Long> clickInterval();

}
