package com.quester.experiment.dagger2experiment.data.quest;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
@JsonObject
public class QuestMetaData {

    @JsonField
    private long originalId;
    @JsonField
    private long version;

    @ParcelConstructor
    public QuestMetaData() {
    }

    public long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(long originalId) {
        this.originalId = originalId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    //TODO: implement

}
