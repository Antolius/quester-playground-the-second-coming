package com.quester.experiment.dagger2experiment.data.game;

import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.json.JSONException;

/**
 * Created by Josip on 14/01/2015!
 */
public class GameState extends QuestState {

    private static final Logger logger = Logger.instance(GameState.class);

    private PersistentGameObject persistentGameObject;

    public GameState(long questId, Quest quest, String persistentGameObject) {
        super(questId, quest);
        setPersistentGameObject(persistentGameObject);
    }

    public String getPersistentGameObjectAsString() {
        return persistentGameObject.getStringRepresentation();
    }

    public void setPersistentGameObject(String persistentGameObject) {
        if (this.persistentGameObject == null || !this.persistentGameObject.getStringRepresentation().equals(persistentGameObject)) {
            safelySetPersistentGameObject(persistentGameObject);
        }
    }

    private void safelySetPersistentGameObject(String persistentGameObject) {
        try {
            this.persistentGameObject = PersistentGameObject.fromString(persistentGameObject);
        } catch (JSONException e) {
            logger.error("formatting persistentGameObject %s failed with exception message %s", persistentGameObject, e.getMessage());
            throw new MisformattedJsonString();
        }
    }

    public static class MisformattedJsonString extends RuntimeException {
    }
}
