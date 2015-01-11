package com.quester.experiment.dagger2experiment.data.area;

/**
 * Created by Josip on 11/01/2015!
 */
//@Parcel
public abstract class Area {

    public static final int METERS = 1;
    public static final int KILOMETERS = 2;
    public static final int MILES = 3;

    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract boolean isInside(Point point);

    public abstract double distanceFrom(Point point, int messurmentUnit);

    public abstract Circle aproximatingCircle();

//    @ParcelFactory
//    public static Area createDefault(Circle circle, long id) {
//        return new CircularArea(circle, id);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Area area = (Area) o;

        if (id != area.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
