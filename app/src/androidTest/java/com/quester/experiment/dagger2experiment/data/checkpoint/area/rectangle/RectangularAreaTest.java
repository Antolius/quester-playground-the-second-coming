package com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.Circle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class RectangularAreaTest {

    private RectangularArea area;
    private Circle circle;
    private Point point;
    private boolean isInside;

    @Test
    public void approximatingCircleIsCorrectlyCalculated() {

        givenRectangularArea(new Rectangle(new Point(1.0, 1.0), new Point(0.0, 0.0)));

        whenGetApproximatingCircle();

        thenApproximatingCircleIs(new Circle(new Point(0.5, 0.5), sqrt(2.0)/2.0));
    }

    @Test
    public void pointIsInsideTheCircularArea() {

        givenRectangularArea(new Rectangle(new Point(1.0, 1.0), new Point(0.0, 0.0)));
        givenPoint(new Point(0.0, 0.5));

        whenTestIsInside();

        thenPointIsInside(true);
    }

    @Test
    public void pointIsNotInsideTheCircularArea() {

        givenRectangularArea(new Rectangle(new Point(1.0, 1.0), new Point(0.0, 0.0)));
        givenPoint(new Point(2.0, 1.0));

        whenTestIsInside();

        thenPointIsInside(false);
    }

    private void givenRectangularArea(Rectangle rectangle) {
        area = new RectangularArea(rectangle);
    }

    private void givenPoint(Point point){
        this.point = point;
    }

    private void whenGetApproximatingCircle() {
        circle = area.approximatingCircle();
    }

    private void whenTestIsInside() {
        isInside = area.isInside(point);
    }

    private void thenApproximatingCircleIs(Circle expected) {
        assertEquals(expected.getCenter().getLatitude(), circle.getCenter().getLatitude(), 1e-8);
        assertEquals(expected.getCenter().getLongitude(), circle.getCenter().getLongitude(), 1e-8);
        assertEquals(expected.getRadius(), circle.getRadius(), 1e-8);
    }

    private void thenPointIsInside(boolean expected) {
        assertEquals(expected, isInside);
    }
}
