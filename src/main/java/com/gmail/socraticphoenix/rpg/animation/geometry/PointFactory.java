package com.gmail.socraticphoenix.rpg.animation.geometry;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import com.graphbuilder.curve.Point;

public class PointFactory {

    public static Point create(double x, double y) {
        return new Point2D(x, y);
    }

    public static Point create(double x, double y, double z) {
        return new Point3D(x, y, z);
    }

    public static Point convert(Vector3d vector) {
        return create(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Point convert(Vector2d vector) {
        return create(vector.getX(), vector.getY());
    }

}
