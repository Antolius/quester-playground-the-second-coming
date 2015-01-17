package com.quester.experiment.dagger2experiment.engine;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraphUtils;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.engine.state.GameStateProvider;
import com.quester.experiment.dagger2experiment.engine.state.QuestState;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedListener;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;
import com.quester.experiment.dagger2experiment.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;


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

    @Override
    public void onCreate() {
        Logger.v(TAG, "onCreate is called, initiating dependency injection...");

        super.onCreate();

        EngineComponent engineComponent = Dagger_EngineComponent.builder()
                .engineModule(new EngineModule(this))
                .build();
        engineComponent.injectGameEngineService(this);

        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.setCheckpointReachedListener(this);
        }

        Logger.v(TAG, "Injected dependencies");
    }

    @Override
    public void onCheckpointReached(Checkpoint reachedCheckpoint) {
        Logger.d(TAG, "onCheckpointReached called with %s" + reachedCheckpoint.toString());

        for (Processor processor : checkpointVisitabillityProcessors) {
            if (!processor.isCheckpointVisitable(reachedCheckpoint)) {
                return;
            }
        }
        visitCheckpoint(reachedCheckpoint);
    }

    @Override
    protected void stopGame() {
        Logger.v(TAG, "stopping the current game");

        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.stop();
        }
        gameStateProvider.saveGameState();

        isGameInProgress = false;
    }

    @Override
    protected void startGame(Quest quest) {
        Logger.v(TAG, "game starting with quest %s, id=%d", quest.getName(), quest.getId());

        if (isGameInProgress) {
            stopGame();
        }

        gameStateProvider.initiate(quest);
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.start();
        }
        registerReachableCheckpoints(QuestGraphUtils.getRootCheckpoints(quest.getQuestGraph()));

        isGameInProgress = true;
    }

    private void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        for (Trigger trigger : checkpointReachedTriggers) {
            trigger.registerReachableCheckpoints(reachableCheckpoints);
        }
    }

    private void visitCheckpoint(Checkpoint visitedCheckpoint) {
        Logger.d(TAG, "visited checkpoint %s", visitedCheckpoint.toString());
        sendNotification(visitedCheckpoint);

        QuestState currentQuestState = gameStateProvider.getGameState().getQuestState();
        currentQuestState.setCheckpointAsVisited(visitedCheckpoint);

        Set<Checkpoint> reachableCheckpoints = currentQuestState.getQuestGraph().getChildren(visitedCheckpoint);
        if (!reachableCheckpoints.isEmpty()) {
            registerReachableCheckpoints(reachableCheckpoints);
            gameStateProvider.saveGameState();
            return;
        }

        //TODO: finish the game

    }

    //TODO: refactor!
    private void sendNotification(Checkpoint visitedCheckpoint) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("New Checkpoint reached")
                        .setContentText("Checkpoint id=" + visitedCheckpoint.getId());

        int mNotificationId = (int) visitedCheckpoint.getId();
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}