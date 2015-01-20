package com.quester.experiment.dagger2experiment.engine;

import android.content.Context;

import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.engine.processor.javascript.JavaScriptProcessor;
import com.quester.experiment.dagger2experiment.engine.trigger.Trigger;
import com.quester.experiment.dagger2experiment.engine.trigger.location.LocationTrigger;
import com.quester.experiment.dagger2experiment.engine.trigger.time.TimeTrigger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class EngineModule {

    public static final String TAG = "EngineModule";

    private final GameEngineService service;

    public EngineModule(GameEngineService service) {
        this.service = service;
    }

    @Provides
    @Singleton
    @EngineScope
    public Context provideServiceContext() {
        return service;
    }


    /*
     * update parameters of following provider with additional triggers:
     */
    @Provides
    @Singleton
    public List<Trigger> provideTriggers(LocationTrigger locationTrigger, TimeTrigger timeTrigger) {

        //testing configuration uses only time trigger for convenience
        ArrayList<Trigger> triggers = new ArrayList<>(1);
//        triggers.add(locationTrigger);
        triggers.add(timeTrigger);

        return triggers;
    }

    /*
     * update parameters of following provider with additional processors:
     */
    @Provides
    @Singleton
    public List<Processor> provideProcessors(JavaScriptProcessor javaScriptProcessor) {
        ArrayList<Processor> processors = new ArrayList<>(1);
        processors.add(javaScriptProcessor);
        return processors;
    }
}
