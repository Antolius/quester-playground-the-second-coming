package com.quester.experiment.dagger2experiment.data.quest;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.quester.experiment.dagger2experiment.archive.QuestGraphConverter;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by Josip on 11/01/2015!
 */
@Parcel(Parcel.Serialization.METHOD)
@JsonObject
public class Quest {

    @JsonField
    private long id;
    @JsonField
    private String name;
    @JsonField(typeConverter = QuestGraphConverter.class)
    private QuestGraph questGraph;
    @JsonField
    private QuestMetaData questMetaData;

    public Quest() {
    }

    @ParcelConstructor
    public Quest(long id, String name, QuestGraph questGraph, QuestMetaData questMetaData) {
        this.id = id;
        this.name = name;
        this.questGraph = questGraph;
        this.questMetaData = questMetaData;
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

    public QuestGraph getQuestGraph() {
        return questGraph;
    }

    public void setQuestGraph(QuestGraph questGraph) {
        this.questGraph = questGraph;
    }

    public QuestMetaData getQuestMetaData() {
        return questMetaData;
    }

    public void setQuestMetaData(QuestMetaData questMetaData) {
        this.questMetaData = questMetaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Quest quest = (Quest) o;

        if (id != quest.id) {
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
        return "Quest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", questGraph=" + questGraph +
                ", questMetaData=" + questMetaData +
                '}';
    }
}
