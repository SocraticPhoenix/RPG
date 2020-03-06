package com.gmail.socraticphoenix.rpg.animation.geometry;

import com.flowpowered.math.vector.Vector3d;

public class LineIterator {
    private Vector3d start;
    private Vector3d end;
    private double distanceSq;

    private Vector3d increment;

    private Vector3d current;

    public LineIterator(Vector3d start, Vector3d end, double distance) {
        increment = end.sub(start).normalize().mul(distance);
        this.start = start;
        this.end = end;
        this.current = start;
        this.distanceSq = start.distanceSquared(end);
    }

    public LineIterator(Vector3d start, Vector3d end, int pointNum) {
        this(start, end, start.distance(end) / pointNum);
    }

    public Vector3d next() {
        Vector3d res = current;
        current = current.add(increment);
        return res;
    }

    public boolean hasNext() {
        return current.distanceSquared(start) <= distanceSq;
    }

}
