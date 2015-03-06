package com.quester.experiment.dagger2experiment.engine.trigger;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import java.util.Collection;

public interface Trigger {

    /**
     * sets a callback function on the trigger
     * that should be invoked when a checkpoint is triggered
     * @param callback the callback function
     */
    public void setCheckpointReachedListener(CheckpointReachedListener callback);

    /**
     * sets a collection of checkpoints that can be triggered
     * @param reachableCheckpoints collection of checkpoints
     */
    public void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints);

    /**
     * starts the process of checking if any of the registered checkpoints is triggere
     */
    public void start();

    /**
     * stops the process of checking for triggered checkpoints
     */
    public void stop();

}
