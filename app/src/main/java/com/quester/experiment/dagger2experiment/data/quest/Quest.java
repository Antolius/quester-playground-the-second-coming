package com.quester.experiment.dagger2experiment.data.quest;

/**
 * Created by Josip on 11/01/2015!
 */
public class Quest {

    private long id;
    private String name;
    private QuestGraph questGraph;
    private QuestMetaData questMetaData;

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
