package com.gmail.socraticphoenix.rpg.animation.frame;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.collect.coupling.Pair;
import com.gmail.socraticphoenix.rpg.animation.pixel.AnimationPixel;
import com.gmail.socraticphoenix.rpg.animation.unit.AnimationUnit;
import com.gmail.socraticphoenix.rpg.animation.unit.AnimationUnitIterator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnimationPartialUnitFrame implements AnimationFrame {
    private AnimationUnit unit;
    private AnimationPixel pixel;
    private int pointCount;

    public AnimationPartialUnitFrame(AnimationUnit unit, AnimationPixel pixel, int pointCount) {
        this.unit = unit;
        this.pointCount = pointCount;
        this.pixel = pixel;
    }

    @Override
    public AnimationFrameIterator iterator() {
        return new AnimationFrameIterator() {
            AnimationUnitIterator iterator = unit.iterator();

            @Override
            public Collection<Pair<Vector3d, AnimationPixel>> nextFrame() {
                Set<Pair<Vector3d, AnimationPixel>> locations = new HashSet<>();
                for (int i = 0; i < pointCount && iterator.hasNext(); i++) {
                    locations.add(Pair.of(iterator.next(), pixel));
                }

                return locations;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

}
