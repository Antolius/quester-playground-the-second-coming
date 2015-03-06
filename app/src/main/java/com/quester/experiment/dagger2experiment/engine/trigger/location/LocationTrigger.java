package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.EngineScope;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedListener;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;
import com.quester.experiment.dagger2experiment.util.Logger;

import java.util.Collection;

import javax.inject.Inject;

import static com.quester.experiment.dagger2experiment.engine.trigger.location.Constants.*;

public class LocationTrigger extends BroadcastReceiver implements Trigger {

    public static final String TAG = "LocationTrigger";

    private CheckpointReachedListener listener;

    private Context context;

    private GeofencesTracker geofencesTracker;

    @Inject
    public LocationTrigger(@EngineScope Context context, GeofencesTracker geofencesTracker) {
        this.context = context;
        this.geofencesTracker = geofencesTracker;
    }

    @Override
    public void setCheckpointReachedListener(CheckpointReachedListener listener) {
        Logger.verbose(TAG, "setting checkpointReachedListener");

        this.listener = listener;
    }

    @Override
    public void start() {
        context.registerReceiver(this, new IntentFilter(GEOFENCE_ENTERED_ACTION));
        geofencesTracker.start();
    }

    @Override
    public void stop() {
        geofencesTracker.stop();
        context.unregisterReceiver(this);
    }

    @Override
    public void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        geofencesTracker.trackCheckpoints(reachableCheckpoints);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Logger.debug(TAG, "onReceive is called");

        Checkpoint triggeringCheckpoint = geofencesTracker
                .getTrackingCheckpointById(extractCheckpointIdFromIntent(intent));
        Location location = extractLocationFromIntent(intent);

        Logger.verbose(TAG, "triggeringCheckpoint=%s, location=%s", triggeringCheckpoint, location);

        if (triggeringCheckpoint.getArea().isInside(new Point(location))) {
            listener.onCheckpointReached(triggeringCheckpoint);
        }
    }

    private long extractCheckpointIdFromIntent(Intent intent) {
        try {
            return Long.valueOf(intent.getStringExtra(CHECKPOINT_ID_EXTRA_ID));
        } catch (NullPointerException exception) {
            Logger.error(TAG, "exception extracting checkpoint id from intent");
            throw new IllegalArgumentException("Missing checkpoint id as string extra in intent");
        }
    }

    private Location extractLocationFromIntent(Intent intent) {
        Location location = intent.getParcelableExtra(LOCATION_EXTRA_ID);

        if (location == null) {
            Logger.error(TAG, "no location found in intent");
            throw new IllegalArgumentException("Missing Location as parcelable extra in the intent");
        }

        return location;
    }
}
