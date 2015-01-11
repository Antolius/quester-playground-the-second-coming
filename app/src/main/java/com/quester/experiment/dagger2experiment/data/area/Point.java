package com.quester.experiment.dagger2experiment.data.area;

import android.location.Location;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
public class Point {

    private double latitude;
    private double longitude;

    public Point(final Location location) {
        this(location.getLatitude(), location.getLongitude());
    }

    @ParcelConstructor
    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
