package com.quester.experiment.dagger2experiment.data.checkpoint.area;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.Circle;

public interface Area {

    boolean isInside(Point point);

    double distanceFrom(Point point, DistanceUnit unit);

    Circle approximatingCircle();

}
