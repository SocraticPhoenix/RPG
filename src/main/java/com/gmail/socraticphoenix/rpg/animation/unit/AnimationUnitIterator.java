package com.gmail.socraticphoenix.rpg.animation.unit;

import com.flowpowered.math.vector.Vector3d;

public interface AnimationUnitIterator {

    boolean hasNext();

    Vector3d next();

}
