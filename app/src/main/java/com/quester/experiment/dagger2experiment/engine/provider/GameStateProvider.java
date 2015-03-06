package com.quester.experiment.dagger2experiment.engine.provider;

import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.game.GameStateRepository;
import com.quester.experiment.dagger2experiment.util.Logger;

public class GameStateProvider {

    public static final String TAG = "GameStateProvider";

    private final GameStateRepository repository;

    private GameState gameState;
    private long currentQuestId;

    public GameStateProvider(GameStateRepository repository) {
        this.repository = repository;
    }

    public void initiate(Quest quest) {
        Logger.verbose(TAG, "initiated with quest %s, id=%debug", quest.getName(), quest.getId());

        this.currentQuestId = quest.getId();
        if(repository.exists(currentQuestId)){
            gameState = repository.findOne(currentQuestId);
            return;
        }

        gameState = new GameState(currentQuestId, quest, "{}");
        repository.save(gameState);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void saveGameState() {
        Logger.verbose(TAG, "saved game state for quest id=%debug", currentQuestId);
        repository.save(gameState);
    }
}
