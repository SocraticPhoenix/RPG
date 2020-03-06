package com.gmail.socraticphoenix.rpg.click.data;

import com.gmail.socraticphoenix.rpg.click.ItemClickPredicate;
import com.gmail.socraticphoenix.rpg.click.data.impl.ItemClickDataImpl;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface ItemClickData extends DataManipulator<ItemClickData, ImmutableItemClickData> {

    static ItemClickData of(ItemClickPredicate predicate, long interval) {
        return new ItemClickDataImpl(predicate, interval);
    }

    Value<ItemClickPredicate> predicate();

    Value<Long> clickInterval();
    
}
