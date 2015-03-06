package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.Circle;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.android.gms.location.Geofence.*;
import static com.google.android.gms.location.LocationServices.GeofencingApi;

public class GeofenceApiUtils {

    public static final int LOITERING_DELAY = 1000;

    public static void removeGeofencesForCheckpoints(GoogleApiClient apiClient,
                                                     Collection<Checkpoint> checkpoints) {

        GeofencingApi.removeGeofences(apiClient, getIdsToRemove(checkpoints));
    }

    private static ArrayList<String> getIdsToRemove(Collection<Checkpoint> checkpoints) {

        ArrayList<String> geofenceIdsToRemove = new ArrayList<>(checkpoints.size());
        for (Checkpoint checkpoint : checkpoints) {
            geofenceIdsToRemove.add(String.valueOf(checkpoint.getId()));
        }
        return geofenceIdsToRemove;
    }

    public static void addGeofencesForCheckpoints(Context context,
                                                  GoogleApiClient apiClient,
                                                  Collection<Checkpoint> checkpoints) {
        GeofencingApi.addGeofences(
                apiClient,
                generateGeofencingRequestForCheckpoints(checkpoints),
                generatePendingRequest(context));
    }

    private static GeofencingRequest generateGeofencingRequestForCheckpoints(Collection<Checkpoint> checkpoints) {
        return new GeofencingRequest.Builder()
                .addGeofences(generateGeofencesFromCheckpoints(checkpoints))
                .build();
    }

    private static ArrayList<Geofence> generateGeofencesFromCheckpoints(Collection<Checkpoint> checkpoints) {

        ArrayList<Geofence> geofences = new ArrayList<>(checkpoints.size());

        for (Checkpoint checkpoint : checkpoints) {
            geofences.add(generateGeofenceFromCheckpoint(checkpoint));
        }

        return geofences;
    }

    private static Geofence generateGeofenceFromCheckpoint(Checkpoint checkpoint) {

        Circle circle = checkpoint.getArea().approximatingCircle();

        return new Builder()
                .setRequestId(String.valueOf(checkpoint.getId()))
                .setTransitionTypes(GEOFENCE_TRANSITION_ENTER | GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(LOITERING_DELAY)
                .setCircularRegion(
                        circle.getCenter().getLatitude(),
                        circle.getCenter().getLongitude(),
                        (float) circle.getRadius())
                .setExpirationDuration(NEVER_EXPIRE)
                .build();
    }

    private static PendingIntent generatePendingRequest(Context context) {
        return PendingIntent.getService(
                context,
                0,
                new Intent(context, GeofenceIntentService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
