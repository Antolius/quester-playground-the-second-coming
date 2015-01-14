package com.quester.experiment.dagger2experiment.engine.processor;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

/**
 * Created by Josip on 14/01/2015!
 */
public interface Processor {

    public boolean isCheckpointVisitable(Checkpoint reachedCheckpoint);

}
