package com.quester.experiment.dagger2experiment.engine.state;

import com.quester.experiment.dagger2experiment.data.quest.Quest;

/**
 * Created by Josip on 14/01/2015!
 */
public class GameStateProvider {

    public static final String TAG = "GameStateProvider";

    private GameState gameState;

    public void initiate(Quest quest) {
        gameState = new GameState();

        gameState.setPersistentGameObject(new PersistentGameObject());
        gameState.setQuestState(new QuestState(quest.getQuestGraph()));
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean saveGameState() {
        //TODO: implement

        return true;
    }

}
