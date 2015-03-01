package com.quester.experiment.dagger2experiment.data.checkpoint.area.circle;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CircularAreaTest {

    private CircularArea area;
    private Circle circle;
    private Point point;
    private boolean isInside;

    @Test
    public void approximatingCircleEqualsTheCircularArea() {

        givenCircularArea(new Circle(new Point(0.0, 0.0), 10.0));

        whenGetApproximatingCircle();

        thenApproximatingCircleIs(new Circle(new Point(0.0, 0.0), 10.0));
    }

    @Test
    public void pointIsInsideTheCircularArea() {

        givenCircularArea(new Circle(new Point(0.0, 0.0), 10.0));
        givenPoint(new Point(1.0, 1.0));

        whenTestIsInside();

        thenPointIsInside(true);
    }

    @Test
    public void pointIsNotInsideTheCircularArea() {

        givenCircularArea(new Circle(new Point(0.0, 0.0), 10.0));
        givenPoint(new Point(10.0, 10.0));

        whenTestIsInside();

        thenPointIsInside(false);
    }

    private void givenCircularArea(Circle circle) {
        area = new CircularArea(circle);
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
        assertEquals(expected.getCenter().getLatitude(), circle.getCenter().getLatitude(), 0.0);
        assertEquals(expected.getCenter().getLongitude(), circle.getCenter().getLongitude(), 0.0);
        assertEquals(expected.getRadius(), circle.getRadius(), 0.0);
    }

    private void thenPointIsInside(boolean expected) {
        assertEquals(expected, isInside);
    }
}
