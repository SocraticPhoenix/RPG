package com.gmail.socraticphoenix.rpg.data.sponge;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.Value;

public interface RPGCustomData<T extends RPGData<T>, M extends RPGCustomData<T, M, I>, I extends RPGImmutableCustomData<T, M, I>> extends DataManipulator<M, I> {

    Value<T> value();

}
