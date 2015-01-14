package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.quester.experiment.dagger2experiment.data.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.EngineScope;
import com.quester.experiment.dagger2experiment.engine.trigger.CheckpointReachedCallback;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;

import org.parceler.Parcels;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by Josip on 14/01/2015!
 */
public class LocationTrigger extends BroadcastReceiver implements Trigger {

    private CheckpointReachedCallback callback;

    private Context context;
    private LocationTracker locationTracker;

    @Inject
    public LocationTrigger(@EngineScope Context context, @EngineScope LocationTracker locationTracker) {
        this.context = context;
        this.locationTracker = locationTracker;
    }

    @Override
    public void onCheckpointReached(CheckpointReachedCallback callback) {
        this.callback = callback;
    }

    @Override
    public void start() {
        context.registerReceiver(this, new IntentFilter(IntentIds.GEOFENCE_ENTERED_ACTION));
        locationTracker.start();
    }

    @Override
    public void stop() {
        locationTracker.stop();
    }

    @Override
    public void registerReachableCheckpoints(Collection<Checkpoint> reachableCheckpoints) {
        locationTracker.trackCheckpoints(reachableCheckpoints);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Checkpoint triggeringCheckpoint = extractCheckpointFromIntent(intent);
        Location location = extractLocationFromIntent(intent);

        if (triggeringCheckpoint.getArea().isInside(new Point(location))) {
            callback.reachCheckpoint(triggeringCheckpoint);
        }
    }

    private Checkpoint extractCheckpointFromIntent(Intent intent) {
        try {
            return Parcels.unwrap(intent.getParcelableExtra(IntentIds.CHECKPOINT_EXTRA_ID));
        } catch (Exception exception) {
            //TODO: granulate exception handling
            throw new IllegalArgumentException("Missing Checkpoint as parcelable extra in the intent");
        }
    }

    private Location extractLocationFromIntent(Intent intent) {
        Location location = intent.getParcelableExtra(IntentIds.LOCATION_EXTRA_ID);

        if (location == null) {
            throw new IllegalArgumentException("Missing Location as parcelable extra in the intent");
        }

        return location;
    }
}
