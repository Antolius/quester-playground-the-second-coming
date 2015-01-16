package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Josip on 14/01/2015!
 */
public class GeofenceIntentService extends IntentService {

    public static final String TAG = "GeofenceIntentService";

    public GeofenceIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.v(TAG, "onHandleIntent called with intent=%s", intent);

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        Logger.v(TAG, "triggering event=%s", event);

        Geofence geofence = event.getTriggeringGeofences().get(0);
        Logger.v(TAG, "triggering geofence=%s", geofence);

        Location location = event.getTriggeringLocation();
        Logger.v(TAG, "triggering location=%s", location);


        ArrayList<Parcelable> wrappedCheckpoints = intent.getParcelableArrayListExtra(Constants.CHECKPOINTS_ARRAY_EXTRA_ID);

        //TODO: handle multiple triggering geofences at once
        Parcelable wrappedCheckpoint = getTriggeringCheckpoint(wrappedCheckpoints, geofence);

        Logger.v(TAG, "sending broadcast with checkpoint=%s", wrappedCheckpoint);
        sendBroadcast(new Intent("Entered checkpoint area")
                        .putExtra(Constants.CHECKPOINT_EXTRA_ID, wrappedCheckpoint)
                        .putExtra(Constants.LOCATION_EXTRA_ID, location)
        );
        Logger.v(TAG, "broadcast sent");
    }

    private Parcelable getTriggeringCheckpoint(ArrayList<Parcelable> wrappedCheckpoints, Geofence triggeringGeofence) {
        Checkpoint unwrappedCheckpoint;

        try {
            for (Parcelable wrappedCheckpoint : wrappedCheckpoints) {
                unwrappedCheckpoint = Parcels.unwrap(wrappedCheckpoint);
                if (triggeringGeofence.getRequestId().equals(String.valueOf(unwrappedCheckpoint.getId()))) {
                    return wrappedCheckpoint;
                }
            }
        } catch (Exception ignorable) {
            //TODO: granulate exception handling
        }

        throw new IllegalArgumentException("Missing array list of all checkpoints as extra in the intent.");
    }
}
