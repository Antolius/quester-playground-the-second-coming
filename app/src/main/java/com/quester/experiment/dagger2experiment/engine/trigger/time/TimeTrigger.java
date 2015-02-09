package com.quester.experiment.dagger2experiment.engine.trigger.time;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedListener;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;

import java.util.Collection;

/**
 * Created by Josip on 20/01/2015!
 * To be used for testing purposes only!
 */
public class TimeTrigger implements Trigger {

    public static final String TAG = "TimeTrigger";

    private static final long DELAY = 10 * 1000L;

    private CheckpointReachedListener callback;

    private Checkpoint reachableCheckpoint;

    private TimeDelayedTask currentTask;

    private boolean shouldStop = false;

    @Override
    public void setCheckpointReachedListener(CheckpointReachedListener callback) {
        this.callback = callback;
    }

    @Override
    public void start() {
        shouldStop = false;
    }

    @Override
    public void stop() {
        shouldStop = true;
        currentTask.cancel(true);
    }

    @Override
    public void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        if (reachableCheckpoints == null || reachableCheckpoints.isEmpty()) {
            return;
        }

        reachableCheckpoint = reachableCheckpoints.iterator().next();
        executeNewTask();
    }

    private void executeNewTask() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }

        currentTask = new TimeDelayedTask();
        currentTask.execute();
    }

    private class TimeDelayedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(DELAY);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onCheckpointReached(reachableCheckpoint);

            if (!shouldStop) {
                executeNewTask();
            }
        }
    }
}
