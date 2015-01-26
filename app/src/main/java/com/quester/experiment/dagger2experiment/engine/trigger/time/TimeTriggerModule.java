package com.quester.experiment.dagger2experiment.engine.trigger.time;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 20/01/2015!
 * To be used for testing purposes only!
 */
@Module
public class TimeTriggerModule {

    @Provides
    @Singleton
    public TimeTrigger provideTimeTrigger() {
        return new TimeTrigger();
    }
}
