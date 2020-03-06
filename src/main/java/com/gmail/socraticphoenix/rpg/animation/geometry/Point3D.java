package com.gmail.socraticphoenix.rpg.animation.geometry;

import com.graphbuilder.geom.Point3d;

public class Point3D implements Point3d {
    private double[] location;

    public Point3D(double x, double y, double z) {
        this.location = new double[] {x, y, z};
    }

    @Override
    public void setLocation(double x, double y, double z) {
        location[0] = x;
        location[1] = y;
        location[2] = z;
    }

    @Override
    public double getZ() {
        return location[2];
    }

    @Override
    public void setLocation(double x, double y) {
        location[0] = x;
        location[1] = y;
    }

    @Override
    public double getX() {
        return location[0];
    }

    @Override
    public double getY() {
        return location[1];
    }

    @Override
    public void setLocation(double[] p) {
        if (p.length == 2) {
            location[0] = p[0];
            location[1] = p[1];
        } else {
            location = p;
        }
    }

    @Override
    public double[] getLocation() {
        return location;
    }

}
