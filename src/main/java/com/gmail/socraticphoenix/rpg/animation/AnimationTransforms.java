package com.gmail.socraticphoenix.rpg.animation;

import com.flowpowered.math.vector.Vector3d;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.MatrixType;
import org.spongepowered.api.text.selector.ArgumentHolder;

import java.util.function.Function;

public interface AnimationTransforms {

    static Function<Vector3d, Vector3d> affine(Affine affine) {
        return vector3d -> {
            Point3D transformed = affine.transform(vector3d.getX(), vector3d.getY(), vector3d.getZ());
            return new Vector3d(transformed.getX(), transformed.getY(), transformed.getZ());
        };
    }

    static Function<Vector3d, Vector3d> newAxes(Vector3d xAxis, Vector3d yAxis, Vector3d zAxis) {
        xAxis = xAxis.normalize();
        yAxis = yAxis.normalize();
        zAxis = zAxis.normalize();

        return affine(new Affine(new double[]{
                xAxis.getX(), yAxis.getX(), zAxis.getX(), 0,
                xAxis.getY(), yAxis.getY(), zAxis.getY(), 0,
                xAxis.getZ(), yAxis.getZ(), zAxis.getZ(), 0
        }, MatrixType.MT_3D_3x4, 0));
    }

    static Function<Vector3d, Vector3d> newXAxis(Vector3d xAxis) {
        double y = xAxis.getY();
        y = y < 0 ? y - 1 : y + 1;
        Vector3d arbitrary = new Vector3d(xAxis.getX(), y, xAxis.getZ());

        Vector3d yAxis = xAxis.cross(arbitrary);
        Vector3d zAxis = xAxis.cross(yAxis);

        return newAxes(xAxis, yAxis, zAxis);
    }

    static Function<Vector3d, Vector3d> newYAxis(Vector3d yAxis) {
        double z = yAxis.getZ();
        z = z < 0 ? z - 1 : z + 1;
        Vector3d arbitrary = new Vector3d(yAxis.getX(), yAxis.getY(), z);

        Vector3d xAxis = yAxis.cross(arbitrary);
        Vector3d zAxis = yAxis.cross(xAxis);

        return newAxes(xAxis, yAxis, zAxis);
    }

    static Function<Vector3d, Vector3d> newZAxis(Vector3d zAxis) {
        double x = zAxis.getX();
        x = x < 0 ? x - 1 : x + 1;
        Vector3d arbitrary = new Vector3d(x, zAxis.getY(), zAxis.getZ());

        Vector3d xAxis = zAxis.cross(arbitrary);
        Vector3d yAxis = zAxis.cross(xAxis);

        return newAxes(xAxis, yAxis, zAxis);
    }

}
