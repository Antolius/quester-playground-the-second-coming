package com.quester.experiment.dagger2experiment.data.checkpoint.area.circle;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Area;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.DistanceUnit;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import static com.quester.experiment.dagger2experiment.data.checkpoint.area.AreaUtils.distance;

@Parcel(Parcel.Serialization.METHOD)
public class CircularArea implements Area {

    public static final String CIRCLE_FIELD_NAME = "circularArea";

    private final Circle circle;

    @ParcelConstructor
    public CircularArea(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    @Override
    public boolean isInside(final Point point) {
        return distance(point, circle.getCenter()) <= circle.getRadius();
    }

    @Override
    public double distanceFrom(Point point, DistanceUnit unit) {
        return distance(point, circle.getCenter());
    }

    @Override
    public Circle approximatingCircle() {
        return circle;
    }

}
