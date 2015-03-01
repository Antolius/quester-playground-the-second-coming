package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.quester.experiment.dagger2experiment.util.Logger;

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
        Logger.verbose(TAG, "onHandleIntent called");

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        if (event.hasError()) {
            Logger.error(TAG, "geofencing error with code %debug has occurred", event.getErrorCode());
            return;
        }

        if (event.getGeofenceTransition() == -1) {
            Logger.warning(TAG, "geofence event not describing transition! Triggering intents action is %s", intent.getAction());
            return;
        }

        Geofence geofence = event.getTriggeringGeofences().get(0);
        Logger.verbose(TAG, "triggering geofence=%s", geofence.toString());

        Location location = event.getTriggeringLocation();
        Logger.verbose(TAG, "triggering location=%s", location.toString());

        //TODO: handle multiple triggering geofences at once

        Logger.verbose(TAG, "sending broadcast...");
        sendBroadcast(new Intent(Constants.GEOFENCE_ENTERED_ACTION)
                        .putExtra(Constants.CHECKPOINT_ID_EXTRA_ID, geofence.getRequestId())
                        .putExtra(Constants.LOCATION_EXTRA_ID, location)
        );
    }
}
