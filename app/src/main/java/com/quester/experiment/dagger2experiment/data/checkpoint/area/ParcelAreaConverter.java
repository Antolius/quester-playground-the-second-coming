package com.quester.experiment.dagger2experiment.data.checkpoint.area;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

public class ParcelAreaConverter implements ParcelConverter<Area> {

    @Override
    public void toParcel(Area input, Parcel parcel) {

        parcel.writeParcelable(Parcels.wrap(input), 0);
    }

    @Override
    public Area fromParcel(Parcel parcel) {
        return Parcels.unwrap(parcel.readParcelable(Area.class.getClassLoader()));
    }
}
