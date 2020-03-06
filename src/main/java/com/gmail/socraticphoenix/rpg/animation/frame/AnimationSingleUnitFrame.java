package com.gmail.socraticphoenix.rpg.animation.frame;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;
import com.gmail.socraticphoenix.rpg.animation.unit.AnimationUnit;
import com.gmail.socraticphoenix.rpg.animation.unit.AnimationUnitIterator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnimationSingleUnitFrame implements AnimationFrame {
    private AnimationUnit unit;
    private AnimationPixel pixel;

    public AnimationSingleUnitFrame(AnimationUnit unit, AnimationPixel pixel) {
        this.unit = unit;
        this.pixel = pixel;
    }

    @Override
    public AnimationFrameIterator iterator() {
        return new AnimationFrameIterator() {
            AnimationUnitIterator iterator = unit.iterator();
            private boolean hasNext = true;

            @Override
            public Collection<Pair<Vector3d, AnimationPixel>> nextFrame() {
                hasNext = false;
                Set<Pair<Vector3d, AnimationPixel>> locations = new HashSet<>();

                while (iterator.hasNext()) {
                    locations.add(Pair.of(iterator.next(), pixel));
                }

                return locations;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }
        };
    }

}
