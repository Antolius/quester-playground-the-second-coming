package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.quester.experiment.dagger2experiment.data.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.EngineScope;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedListener;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.parceler.Parcels;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by Josip on 14/01/2015!
 */
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
        Logger.v(TAG, "setting checkpointReachedListener");

        this.listener = listener;
    }

    @Override
    public void start() {
        context.registerReceiver(this, new IntentFilter(Constants.GEOFENCE_ENTERED_ACTION));
        geofencesTracker.start();
    }

    @Override
    public void stop() {
        geofencesTracker.stop();
    }

    @Override
    public void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        geofencesTracker.trackCheckpoints(reachableCheckpoints);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "onReceive is called with intent %s", intent.toString());

        Checkpoint triggeringCheckpoint = extractCheckpointFromIntent(intent);
        Location location = extractLocationFromIntent(intent);

        Logger.v(TAG, "triggeringCheckpoint=%s, location=%s", triggeringCheckpoint, location);

        if (triggeringCheckpoint.getArea().isInside(new Point(location))) {
            listener.onCheckpointReached(triggeringCheckpoint);
        }
    }

    private Checkpoint extractCheckpointFromIntent(Intent intent) {
        try {
            return Parcels.unwrap(intent.getParcelableExtra(Constants.CHECKPOINT_EXTRA_ID));
        } catch (Exception exception) {
            //TODO: granulate exception handling
            Logger.e(TAG, "exception extracting checkpoint from intent %s", exception.getMessage());
            throw new IllegalArgumentException("Missing Checkpoint as parcelable extra in the intent");
        }
    }

    private Location extractLocationFromIntent(Intent intent) {
        Location location = intent.getParcelableExtra(Constants.LOCATION_EXTRA_ID);

        if (location == null) {
            Logger.e(TAG, "no location found in intent");
            throw new IllegalArgumentException("Missing Location as parcelable extra in the intent");
        }

        return location;
    }
}
