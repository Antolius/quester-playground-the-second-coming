package com.quester.experiment.dagger2experiment;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 13/01/2015!
 */
@Module
public class ApplicationModule {

    public static final String TAG = "ApplicationModule";

    private final InjectionApplication application;

    public ApplicationModule(InjectionApplication application) {
        Log.d(TAG, "constructor called with " + application.getClass().getSimpleName());
        this.application = application;
    }

    @Provides
    @Singleton
    @ApplicationScope
    public Context provideApplicationContext() {
        Log.d(TAG, "call to provideApplicationContext, returning " + application.getClass().getSimpleName());
        return application;
    }

    @Provides
    @Singleton
    public LocationManager provideLocationManager() {
        LocationManager locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "call to provideLocationManager, returning " + locationManager.getClass().getSimpleName());
        return locationManager;
    }
}
