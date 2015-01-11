package com.quester.experiment.dagger2experiment.data.area;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import static com.quester.experiment.dagger2experiment.data.area.AreaUtils.nonDimensionalDistance;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
public class CircularArea extends Area {

    private final Circle circle;

    @ParcelConstructor
    public CircularArea(Circle circle, long id) {
        this.id = id;
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    @Override
    public boolean isInside(final Point point) {
        return nonDdimensionalDistanceFrom(point) <= circle.getRadius();
    }

    @Override
    public double distanceFrom(final Point point, final int messurmentUnit) {
        return nonDdimensionalDistanceFrom(point);
    }

    @Override
    public Circle aproximatingCircle() {
        return circle;
    }

    private double nonDdimensionalDistanceFrom(final Point point) {
        return nonDimensionalDistance(point, circle.getCenter());
    }

    @Override
    public String toString() {
        return "CircularArea{" +
                "id=" + id +
                ", circle=" + circle +
                '}';
    }
}
