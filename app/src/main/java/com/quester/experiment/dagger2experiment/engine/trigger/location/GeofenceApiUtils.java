package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.android.gms.location.LocationServices.GeofencingApi;

/**
 * Created by Josip on 14/01/2015!
 */
public class GeofenceApiUtils {

    public static final int LOITERING_DELAY = 1000;

    public static void removeGeofencesForCheckpoints(GoogleApiClient apiClient, Collection<Checkpoint> checkpoints) {
        ArrayList<String> geofenceIdsToRemove = new ArrayList<>(checkpoints.size());
        for (Checkpoint checkpoint : checkpoints) {
            geofenceIdsToRemove.add(String.valueOf(checkpoint.getId()));
        }

        GeofencingApi.removeGeofences(apiClient, geofenceIdsToRemove);
    }

    public static void addGeofencesForCheckpoints(Context context, GoogleApiClient apiClient, Collection<Checkpoint> checkpoints) {
        GeofencingApi.addGeofences(apiClient, generateGeofencingRequestForCheckpoints(checkpoints), generatePendingRequest(context, checkpoints));
    }

    private static GeofencingRequest generateGeofencingRequestForCheckpoints(Collection<Checkpoint> checkpoints) {
        return new GeofencingRequest.Builder().addGeofences(generateGeofanceFromCheckpoints(checkpoints)).build();
    }

    private static ArrayList<Geofence> generateGeofanceFromCheckpoints(Collection<Checkpoint> checkpoints) {
        ArrayList<Geofence> geofences = new ArrayList<>(checkpoints.size());

        for (Checkpoint checkpoint : checkpoints) {
            geofences.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(checkpoint.getId()))
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .setLoiteringDelay(LOITERING_DELAY)
                    .setCircularRegion(checkpoint.getArea().aproximatingCircle().getCenter().getLatitude(),
                            checkpoint.getArea().aproximatingCircle().getCenter().getLongitude(),
                            (float) checkpoint.getArea().aproximatingCircle().getRadius())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build());
        }

        return geofences;
    }

    private static PendingIntent generatePendingRequest(Context context, Collection<Checkpoint> checkpoints) {
        return PendingIntent.getService(
                context,
                0,
                new Intent(context, GeofenceIntentService.class)
                        .putParcelableArrayListExtra(Constants.CHECKPOINTS_ARRAY_EXTRA_ID, wrapCheckpoints(checkpoints)),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static ArrayList<Parcelable> wrapCheckpoints(Collection<Checkpoint> checkpoints) {
        ArrayList<Parcelable> wrappedCheckpoints = new ArrayList<>(checkpoints.size());

        for (Checkpoint checkpoint : checkpoints) {
            wrappedCheckpoints.add(Parcels.wrap(checkpoint));
        }

        return wrappedCheckpoints;
    }

}
