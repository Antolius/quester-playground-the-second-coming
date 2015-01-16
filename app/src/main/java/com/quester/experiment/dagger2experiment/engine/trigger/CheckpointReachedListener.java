package com.quester.experiment.dagger2experiment.engine.trigger;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

/**
 * Created by Josip on 14/01/2015!
 */
public interface CheckpointReachedListener {

    public void onCheckpointReached(Checkpoint reachedCheckpoint);

}