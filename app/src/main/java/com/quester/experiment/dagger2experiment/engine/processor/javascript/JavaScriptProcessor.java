package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import android.os.SystemClock;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.util.Logger;

/**
 * Created by Josip on 14/01/2015!
 */
public class JavaScriptProcessor implements Processor {

    public static final String TAG = "JavaScriptProcessor";

    @Override
    public boolean isCheckpointVisitable(Checkpoint reachedCheckpoint) {
        Logger.d(TAG, "isCheckpointVisitable called with %s", reachedCheckpoint.toString());
        //TODO: implement

        //simulated work:
        SystemClock.sleep(5 * 1000);

        return true;
    }

}
