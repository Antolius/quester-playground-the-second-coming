package com.quester.experiment.dagger2experiment.engine.trigger;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

public interface CheckpointReachedListener {

    /**
     * function that should be invoked after a checkpoint has been
     * triggered and successfully processed as visitable
     * @param reachedCheckpoint checkpoint that is triggered and processed
     */
    public void onCheckpointReached(Checkpoint reachedCheckpoint);

}
