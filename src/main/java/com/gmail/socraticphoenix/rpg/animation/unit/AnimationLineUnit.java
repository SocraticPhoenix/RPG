package com.gmail.socraticphoenix.rpg.animation.unit;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.animation.geometry.LineIterator;

public class AnimationLineUnit implements AnimationUnit {
    private Vector3d start;
    private Vector3d end;
    private double distance;

    public AnimationLineUnit(Vector3d start, Vector3d end, double distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    @Override
    public AnimationUnitIterator iterator() {
        return new AnimationUnitIterator() {
            private LineIterator iterator = new LineIterator(start, end, distance);

            @Override
            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            @Override
            public Vector3d next() {
                return this.iterator.next();
            }

        };
    }

}
