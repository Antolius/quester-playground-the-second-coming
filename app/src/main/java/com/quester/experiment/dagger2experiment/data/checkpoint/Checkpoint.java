package com.quester.experiment.dagger2experiment.data.checkpoint;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Area;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.ParcelAreaConverter;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelPropertyConverter;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
public class Checkpoint {

    private long id;
    private String name;
    private boolean root;
    private Area area;
    private String viewHtmlFileName;
    private String eventsScriptFileName;

    public Checkpoint() {
    }

    @ParcelConstructor
    public Checkpoint(long id, String name, boolean root,@ParcelPropertyConverter(ParcelAreaConverter.class) Area area, String viewHtmlFileName, String eventsScriptFileName) {
        this.id = id;
        this.name = name;
        this.root = root;
        this.area = area;
        this.viewHtmlFileName = viewHtmlFileName;
        this.eventsScriptFileName = eventsScriptFileName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getViewHtmlFileName() {
        return viewHtmlFileName;
    }

    public void setViewHtmlFileName(String viewHtmlFileName) {
        this.viewHtmlFileName = viewHtmlFileName;
    }

    public String getEventsScriptFileName() {
        return eventsScriptFileName;
    }

    public void setEventsScriptFileName(String eventsScriptFileName) {
        this.eventsScriptFileName = eventsScriptFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Checkpoint that = (Checkpoint) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", root=" + root +
                ", area=" + area +
                ", viewHtmlFileName='" + viewHtmlFileName + '\'' +
                ", eventsScriptFileName='" + eventsScriptFileName + '\'' +
                '}';
    }
}
