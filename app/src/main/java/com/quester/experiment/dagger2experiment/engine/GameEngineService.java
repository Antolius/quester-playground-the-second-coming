package com.quester.experiment.dagger2experiment.engine;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.engine.notification.Notifier;
import com.quester.experiment.dagger2experiment.engine.notification.NotifierModule;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateModule;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedListener;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateProvider;
import com.quester.experiment.dagger2experiment.persistence.module.DatabaseModule;
import com.quester.experiment.dagger2experiment.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import static com.quester.experiment.dagger2experiment.data.quest.QuestGraphUtils.getRootCheckpoints;


/**
 * Created by Josip on 14/01/2015!
 */
public class GameEngineService extends GameService implements CheckpointReachedListener {

    public static final String TAG = "GameEngineService";

    boolean isGameInProgress = false;

    @Inject
    protected List<Processor> checkpointVisitabillityProcessors;

    @Inject
    protected List<Trigger> checkpointReachedTriggers;

    @Inject
    protected GameStateProvider gameStateProvider;

    @Inject
    protected Notifier notifier;

    @Override
    public void onCreate() {
        Logger.verbose(TAG, "onCreate is called, initiating dependency injection...");
        super.onCreate();

        buildEngineComponent();

        Logger.verbose(TAG, "Injected dependencies");
    }

    private void buildEngineComponent() {
        EngineComponent engineComponent = Dagger_EngineComponent.builder()
                .engineModule(new EngineModule(this))
                .notifierModule(new NotifierModule(this))
                .gameStateModule(new GameStateModule(this))
                .build();
        engineComponent.injectGameEngineService(this);
    }

    @Override
    public void onCheckpointReached(Checkpoint reachedCheckpoint) {
        Logger.debug(TAG, "onCheckpointReached called with %s", reachedCheckpoint.toString());

        for (Processor processor : checkpointVisitabillityProcessors) {
            if (!processor.isCheckpointVisitable(reachedCheckpoint)) {
                return;
            }
        }
        visitCheckpoint(reachedCheckpoint);
    }

    private void visitCheckpoint(Checkpoint visitedCheckpoint) {
        Logger.debug(TAG, "visited checkpoint %s", visitedCheckpoint.toString());

        notifier.notifyCheckpointReached(visitedCheckpoint);

        GameState gameState = gameStateProvider.getGameState();
        gameState.setCheckpointAsVisited(visitedCheckpoint);

        Set<Checkpoint> reachableCheckpoints = gameState.getQuestGraph().getChildren(visitedCheckpoint);

        if (reachableCheckpoints.isEmpty()) {
            //TODO: finish the game
            stopGame();
            return;
        }

        registerReachableCheckpoints(reachableCheckpoints);
        gameStateProvider.saveGameState();
    }

    @Override
    protected void stopGame() {
        Logger.verbose(TAG, "stopping the current game");

        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.stop();
        }
        gameStateProvider.saveGameState();

        isGameInProgress = false;
    }

    @Override
    protected void startGame(Quest quest) {
        Logger.verbose(TAG, "game starting with quest %s, id=%debug", quest.getName(), quest.getId());

        if (isGameInProgress) {
            stopGame();
        }

        gameStateProvider.initiate(quest);
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.setCheckpointReachedListener(this);
            trigger.start();
        }

        registerReachableCheckpoints(getRootCheckpoints(quest.getQuestGraph()));

        isGameInProgress = true;
    }

    private void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.registerReachableCheckpoints(reachableCheckpoints);
        }
    }

}