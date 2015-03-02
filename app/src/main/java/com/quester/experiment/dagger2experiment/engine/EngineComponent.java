package com.quester.experiment.dagger2experiment.engine;

import com.quester.experiment.dagger2experiment.engine.notification.NotifierModule;
import com.quester.experiment.dagger2experiment.engine.processor.javascript.JavaScriptProcessorModule;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateModule;
import com.quester.experiment.dagger2experiment.engine.trigger.location.LocationTriggerModule;
import com.quester.experiment.dagger2experiment.engine.trigger.time.TimeTriggerModule;
import com.quester.experiment.dagger2experiment.persistence.module.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Josip on 16/01/2015!
 */
@Singleton
@EngineScope
@Component(modules = {
        GameStateModule.class,
        NotifierModule.class,
        LocationTriggerModule.class,
        JavaScriptProcessorModule.class,
        EngineModule.class,
        /*
         * To be used for testing purposes only!
         */
        TimeTriggerModule.class

        /*
         * add new modules here
         */

})

public interface EngineComponent {

    void injectGameEngineService(GameEngineService service);

}
