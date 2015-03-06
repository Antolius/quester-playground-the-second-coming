package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.quester.experiment.dagger2experiment.util.Logger;

import static com.quester.experiment.dagger2experiment.engine.trigger.location.Constants.*;

/**
 * Created by Josip on 14/01/2015!
 */
public class GeofenceIntentService extends IntentService {

    private static final Logger logger = Logger.instance(GeofenceIntentService.class);

    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        logger.verbose("onHandleIntent called");

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        if (event.hasError()) {
            logger.error( "geofencing error with code %debug has occurred", event.getErrorCode());
            return;
        }

        if (event.getGeofenceTransition() == -1) {
            logger.warning("geofence event not describing transition! Triggering intents action is %s", intent.getAction());
            return;
        }

        Geofence geofence = event.getTriggeringGeofences().get(0);
        logger.verbose("triggering geofence=%s", geofence.toString());

        Location location = event.getTriggeringLocation();
        logger.verbose("triggering location=%s", location.toString());

        //TODO: handle multiple triggering geofences at once

        logger.verbose("sending broadcast...");
        sendBroadcast(new Intent(GEOFENCE_ENTERED_ACTION)
                        .putExtra(CHECKPOINT_ID_EXTRA_ID, geofence.getRequestId())
                        .putExtra(LOCATION_EXTRA_ID, location)
        );
    }
}
