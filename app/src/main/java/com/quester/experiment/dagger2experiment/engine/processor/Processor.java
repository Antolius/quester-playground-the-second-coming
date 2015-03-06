package com.quester.experiment.dagger2experiment.engine.processor;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

public interface Processor {

    /**
     * checks if the reached checkpoint can be visited depending on the underlying processing
     * @param reachedCheckpoint checkpoint on which you arrived
     * @return true if the checkpoint can be displayed
     */
    public boolean isCheckpointVisitable(Checkpoint reachedCheckpoint);

}
