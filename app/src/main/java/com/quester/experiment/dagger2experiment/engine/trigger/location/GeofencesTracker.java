package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.EngineScope;
import com.quester.experiment.dagger2experiment.util.Logger;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by Josip on 14/01/2015!
 */
public class GeofencesTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "GeofencesTracker";

    private GoogleApiClient apiClient;
    private Context context;

    private Collection<Checkpoint> trackingCheckpoints = new ArrayList<>(0);

    @Inject
    public GeofencesTracker(@EngineScope Context context) {
        this.context = context;

        apiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Logger.v(TAG, "build new apiClient=%s", apiClient.toString());
    }

    public void trackCheckpoints(Collection<Checkpoint> newTrackingCheckpoints) {
        if (apiClient.isConnected()) {
            switchToTrackingNewCheckpoints(newTrackingCheckpoints);
        }
        trackingCheckpoints = newTrackingCheckpoints;

        Logger.d(TAG, "registered geofences for checkpoints=%s", trackingCheckpoints);
    }

    public void start() {
        if (!apiClient.isConnected()) {
            Logger.v(TAG, "connecting GoogleApiClient...");

            apiClient.connect();
        }
    }

    public void stop() {
        if (apiClient.isConnected()) {
            Logger.v(TAG, "removing geofences and disconnecting GoogleApiClient...");

            GeofenceApiUtils.removeGeofencesForCheckpoints(apiClient, trackingCheckpoints);
            apiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Logger.v(TAG, "GoogleApiClient connected, adding geofences");

        GeofenceApiUtils.addGeofencesForCheckpoints(context, apiClient, trackingCheckpoints);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.w(TAG, "GoogleApiClients connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Logger.e(TAG, "GoogleApiClients connection failed with connectionResult=%s", connectionResult.toString());
    }

    private void switchToTrackingNewCheckpoints(Collection<Checkpoint> newCheckpoints) {
        GeofenceApiUtils.removeGeofencesForCheckpoints(apiClient, trackingCheckpoints);
        GeofenceApiUtils.addGeofencesForCheckpoints(context, apiClient, newCheckpoints);
    }
}