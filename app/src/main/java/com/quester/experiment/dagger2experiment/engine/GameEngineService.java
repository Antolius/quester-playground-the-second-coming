package com.quester.experiment.dagger2experiment.engine;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraphUtils;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.engine.state.GameStateProvider;
import com.quester.experiment.dagger2experiment.engine.state.QuestState;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedCallback;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Josip on 14/01/2015!
 */
public class GameEngineService extends GameService implements CheckpointReachedCallback {

    public static final String TAG = "GameEngineService";

    @Inject
    protected List<Processor> checkpointVisitabillityProcessors;

    @Inject
    protected List<Trigger> checkpointReachedTriggers;

    @Inject
    protected GameStateProvider gameStateProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        EngineComponent engineComponent = Dagger_EngineComponent.builder()
                .engineModule(new EngineModule(this))
                .build();

        engineComponent.injectGameEngineService(this);
    }

    @Override
    public void reachCheckpoint(Checkpoint reachedCheckpoint) {
        for (Processor processor : checkpointVisitabillityProcessors) {
            if (!processor.isCheckpointVisitable(reachedCheckpoint)) {
                return;
            }
        }
        visitCheckpoint(reachedCheckpoint);
    }

    @Override
    protected void stopGame() {
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.stop();
        }
    }

    @Override
    protected void startGame(Quest quest) {
        gameStateProvider.initiate(quest);
        startTriggers();
        registerReachableCheckpoints(QuestGraphUtils.getRootCheckpoints(quest.getQuestGraph()));
    }

    private void startTriggers() {
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.start();
        }
    }

    private void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.registerReachableCheckpoints(reachableCheckpoints);
        }
    }

    private void visitCheckpoint(Checkpoint reachedCheckpoint) {
        QuestState currentQuestState = gameStateProvider.getGameState().getQuestState();

        currentQuestState.setCheckpointAsVisited(reachedCheckpoint);
        registerReachableCheckpoints(currentQuestState.getQuestGraph().getChildren(reachedCheckpoint));

        gameStateProvider.saveGameState();
    }
}
