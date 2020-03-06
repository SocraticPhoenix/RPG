package com.gmail.socraticphoenix.rpg.data.sponge;

import com.gmail.socraticphoenix.rpg.data.RPGData;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public interface RPGImmutableCustomData<T extends RPGData<T>, M extends RPGCustomData<T, M, I>, I extends RPGImmutableCustomData<T, M, I>> extends ImmutableDataManipulator<I, M> {

    ImmutableValue<T> value();

}
