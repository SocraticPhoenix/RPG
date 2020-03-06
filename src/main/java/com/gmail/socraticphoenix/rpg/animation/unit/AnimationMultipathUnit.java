package com.gmail.socraticphoenix.rpg.animation.unit;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.socraticphoenix.rpg.animation.geometry.LineIterator;
import com.gmail.socraticphoenix.rpg.animation.geometry.PointFactory;
import com.graphbuilder.curve.*;

import java.util.List;
import java.util.function.Function;

public class AnimationMultipathUnit implements AnimationUnit {
    private double pointDistance;
    private Vector3d start;
    private MultiPath path;

    public AnimationMultipathUnit(List<Vector3d> controls, double pointDistance, Function<ControlPath, Curve> curveMaker) {
        this.pointDistance = pointDistance;

        ControlPath path = new ControlPath();
        controls.forEach(v -> path.addPoint(PointFactory.convert(v)));

        Curve c = curveMaker.apply(path);
        this.path = new MultiPath(3);
        c.appendTo(this.path);

        this.start = controls.get(0);
    }

    public AnimationMultipathUnit(MultiPath path, double pointDistance) {
        if (path.getNumPoints() < 2) {
            throw new IllegalArgumentException("MultiPath too small ( < 2 points )");
        } else if (path.getDimension() != 3) {
            throw new IllegalArgumentException("MultiPath has the wrong dimension ( dimension != 3 )");
        }

        double[] prev = path.get(0);
        this.start = new Vector3d(prev[0], prev[1], prev[2]);
    }

    @Override
    public AnimationUnitIterator iterator() {
        return new AnimationUnitIterator() {
            private int index = 0;
            private LineIterator currentLine;
            private Vector3d previousPoint = start;

            @Override
            public boolean hasNext() {
                return index < path.getNumPoints() || (currentLine != null && currentLine.hasNext());
            }

            @Override
            public Vector3d next() {
                if (currentLine != null && currentLine.hasNext()) {
                    return currentLine.next();
                } else {
                    double[] pt = path.get(index);

                    if (pt == null) {
                        return previousPoint;
                    }

                    Vector3d end = new Vector3d(pt[0], pt[1], pt[2]);
                    Object type = path.getType(index);
                    if (type == MultiPath.LINE_TO) {
                        try {
                            end.sub(previousPoint).normalize();
                        } catch (ArithmeticException e) {
                            index++;
                            return previousPoint;
                        }
                        LineIterator iterator = new LineIterator(previousPoint, end, pointDistance);
                        previousPoint = end;
                        currentLine = iterator;
                        index++;
                        return iterator.next();
                    } else if (type == MultiPath.MOVE_TO) {
                        Vector3d next = new Vector3d(pt[0], pt[1], pt[2]);
                        previousPoint = next;
                        index++;
                        return next;
                    }
                }

                return null;
            }
        };
    }

}
