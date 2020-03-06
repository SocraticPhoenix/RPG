package com.gmail.socraticphoenix.rpg.animation.frame;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;

import java.util.Collection;

public interface AnimationFrameIterator {

    Collection<Pair<Vector3d, AnimationPixel>> nextFrame();

    boolean hasNext();

}
