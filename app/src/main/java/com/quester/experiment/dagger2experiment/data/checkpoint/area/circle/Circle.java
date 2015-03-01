package com.quester.experiment.dagger2experiment.data.checkpoint.area.circle;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
public class Circle {
    private Point center;
    private double radius;

    @ParcelConstructor
    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
