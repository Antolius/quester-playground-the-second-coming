package com.quester.experiment.dagger2experiment;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 13/01/2015!
 * Application level injection module.
 * Contains providers for all injectable resources.
 */
@Module
public class ApplicationModule {

    public static final String TAG = "ApplicationModule";

    private final InjectionApplication application;

    /**
     * @param application current @see Application instance is used for instantiation of application dependent android services.
     */
    public ApplicationModule(InjectionApplication application) {
        Log.d(TAG, "constructor called with " + application.getClass().getSimpleName());
        this.application = application;
    }

    /**
     * @return
     * @see android.content.Context provider which provides current application context.
     * Used for demonstrating use of @see javax.inject.Qualifier.
     */
    @Provides
    @Singleton
    @ApplicationScope
    public Context provideApplicationContext() {
        Log.d(TAG, "call to provideApplicationContext, returning " + application.getClass().getSimpleName());
        return application;
    }

    /**
     * @return
     * @see LocationManager provider used for demo purposis.
     */
    @Provides
    @Singleton
    public LocationManager provideLocationManager() {
        LocationManager locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "call to provideLocationManager, returning " + locationManager.getClass().getSimpleName());
        return locationManager;
    }
}
