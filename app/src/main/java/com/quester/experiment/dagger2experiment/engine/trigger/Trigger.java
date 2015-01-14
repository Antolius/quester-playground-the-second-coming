package com.quester.experiment.dagger2experiment.engine.trigger;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import java.util.Collection;

/**
 * Created by Josip on 14/01/2015!
 */
public abstract class Trigger {

    protected CheckpointReachedCallback callback;

    public void onCheckpointReached(CheckpointReachedCallback callback) {
        this.callback = callback;
    }

    public abstract void start();

    public abstract void stop();

    abstract public void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints);

}
