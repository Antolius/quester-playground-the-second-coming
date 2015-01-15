package com.quester.experiment.dagger2experiment.engine.trigger.location;

import android.content.Context;

import com.quester.experiment.dagger2experiment.engine.EngineScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class LocationTriggerModule {

    /*
     * it is enough to provide only the loves level dependencies
     * (ie, there is no need to explicitly list LocationTrigger)
     */

    @Provides
    @Singleton
    public GeofencesTracker provideGeofenceTracker(@EngineScope Context context) {
        return new GeofencesTracker(context);
    }

}
