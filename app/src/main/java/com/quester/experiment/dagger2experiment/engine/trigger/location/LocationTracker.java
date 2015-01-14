package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.EngineScope;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by Josip on 14/01/2015!
 */
public class LocationTracker implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient apiClient;
    private Context context;

    private Collection<Checkpoint> trackingCheckpoints = new ArrayList<>(0);

    @Inject
    public LocationTracker(@EngineScope Context context) {
        this.context = context;

        apiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void trackCheckpoints(Collection<Checkpoint> newTrackingCheckpoints) {
        if (apiClient.isConnected()) {
            switchToTrackingNewCheckpoints(newTrackingCheckpoints);
        }

        trackingCheckpoints = newTrackingCheckpoints;
    }

    public void start() {
        if (!apiClient.isConnected()) {
            apiClient.connect();
        }
    }

    public void stop() {
        if (apiClient.isConnected()) {
            GeofenceApiUtils.removeGeofencesForCheckpoints(apiClient, trackingCheckpoints);
            apiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        GeofenceApiUtils.addGeofencesForCheckpoints(context, apiClient, trackingCheckpoints);
    }

    @Override
    public void onConnectionSuspended(int i) {
        //TODO: implement
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //TODO: implement
    }

    private void switchToTrackingNewCheckpoints(Collection<Checkpoint> newCheckpoints) {
        GeofenceApiUtils.removeGeofencesForCheckpoints(apiClient, trackingCheckpoints);
        GeofenceApiUtils.addGeofencesForCheckpoints(context, apiClient, newCheckpoints);
    }
}
