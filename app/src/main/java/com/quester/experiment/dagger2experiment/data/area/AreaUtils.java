package com.quester.experiment.dagger2experiment.data.area;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Josip on 11/01/2015!
 */
public class AreaUtils {

    static double nonDimensionalDistance(final Point pointA, final Point pointB) {
        return sqrt(pow(pointA.getLatitude() - pointB.getLatitude(), 2) + pow(pointA.getLongitude() - pointB.getLongitude(), 2));
    }

    /**
     * @param nonDimensionalValue distance value without measure
     * @param measurementUnit measurement unit to convert nonDimensionalValue to @see com.quester.experiment.dagger2experiment.data.area.Area
     * @return distance in specified measurement unit
     */
    static double convertToDimension(final double nonDimensionalValue, int measurementUnit) {
        //TODO: implement!
        return nonDimensionalValue;
    }

}
