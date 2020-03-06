package com.gmail.socraticphoenix.rpg.animation.geometry;

import com.graphbuilder.geom.Point2d;

public class Point2D implements Point2d {
    private double[] location;

    public Point2D(double x, double y) {
        this.location = new double[] {x, y};
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
        location = p;
    }

    @Override
    public double[] getLocation() {
        return location;
    }

}
