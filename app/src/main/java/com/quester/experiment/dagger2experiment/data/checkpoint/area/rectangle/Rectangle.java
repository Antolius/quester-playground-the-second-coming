package com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.METHOD)
public class Rectangle {

    private Point upperLeftCorner;
    private Point lowerRightCorner;

    @ParcelConstructor
    public Rectangle(Point upperLeftCorner, Point lowerRightCorner) {
        this.upperLeftCorner = upperLeftCorner;
        this.lowerRightCorner = lowerRightCorner;
    }

    public Point getUpperLeftCorner() {
        return upperLeftCorner;
    }

    public void setUpperLeftCorner(Point upperLeftCorner) {
        this.upperLeftCorner = upperLeftCorner;
    }

    public Point getLowerRightCorner() {
        return lowerRightCorner;
    }

    public void setLowerRightCorner(Point lowerRightCorner) {
        this.lowerRightCorner = lowerRightCorner;
    }
}
