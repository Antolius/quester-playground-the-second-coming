package com.quester.experiment.dagger2experiment.engine.state;

/**
 * Created by Josip on 14/01/2015!
 */
public class GameState {

    private QuestState questState;
    private PersistentGameObject persistentGameObject;

    public QuestState getQuestState() {
        return questState;
    }

    public PersistentGameObject getPersistentGameObject() {
        return persistentGameObject;
    }

    void setQuestState(QuestState questState) {
        this.questState = questState;
    }

    void setPersistentGameObject(PersistentGameObject persistentGameObject) {
        this.persistentGameObject = persistentGameObject;
    }
}
