package com.quester.experiment.dagger2experiment.engine.state;

import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.util.Logger;

/**
 * Created by Josip on 14/01/2015!
 */
public class GameStateProvider {

    public static final String TAG = "GameStateProvider";

    private GameState gameState;

    private String questName;
    private long questId;

    public void initiate(Quest quest) {
        Logger.verbose(TAG, "initiated with quest %s, id=%debug", quest.getName(), quest.getId());

        gameState = new GameState(quest.getQuestGraph(), "{}");

        questName = quest.getName();
        questId = quest.getId();
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean saveGameState() {
        Logger.verbose(TAG, "saved game state for quest id=%debug", questId);
        //TODO: implement

        return true;
    }
}
