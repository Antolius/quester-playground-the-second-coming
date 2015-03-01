package com.quester.experiment.dagger2experiment.data.checkpoint.area;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Josip on 11/01/2015!
 */
public class AreaUtils {

    /**
     * calculates the euclidean distance between points A and B
     * @param pointA first point
     * @param pointB second point
     * @return distance between points
     */
    public static double distance(final Point pointA, final Point pointB) {
        return sqrt(pow(pointA.getLatitude() - pointB.getLatitude(), 2) + pow(pointA.getLongitude() - pointB.getLongitude(), 2));
    }

    /**
     * @param nonDimensionalValue distance value without measure
     * @param measurementUnit measurement unit to convert nonDimensionalValue to @see com.quester.experiment.dagger2experiment.data.checkpoint.area.Area
     * @return distance in specified measurement unit
     */
    static double convertToDimension(final double nonDimensionalValue, int measurementUnit) {
        //TODO: implement!
        return nonDimensionalValue;
    }

}
