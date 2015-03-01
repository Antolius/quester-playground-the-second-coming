package com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Area;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.Circle;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.DistanceUnit;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import static com.quester.experiment.dagger2experiment.data.checkpoint.area.AreaUtils.distance;

@Parcel(Parcel.Serialization.METHOD)
public class RectangularArea implements Area {

    public static final String RECTANGLE_FIELD_NAME = "rectangularArea";

    private Rectangle rectangle;

    @ParcelConstructor
    public RectangularArea(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public boolean isInside(Point point) {
        return point.getLatitude() <= rectangle.getUpperLeftCorner().getLatitude()
                && point.getLatitude() >= rectangle.getLowerRightCorner().getLatitude()
                && point.getLongitude() <= rectangle.getUpperLeftCorner().getLongitude()
                && point.getLongitude() >= rectangle.getLowerRightCorner().getLongitude();
    }

    @Override
    public double distanceFrom(Point point, DistanceUnit unit) {
        return 0;
    }

    @Override
    public Circle approximatingCircle() {
        return new Circle(new Point(
                (rectangle.getUpperLeftCorner().getLatitude()-rectangle.getLowerRightCorner().getLatitude())/2,
                (rectangle.getUpperLeftCorner().getLatitude()-rectangle.getLowerRightCorner().getLatitude())/2),
                distance(rectangle.getUpperLeftCorner(), rectangle.getLowerRightCorner())/2);
    }

}
