package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import com.quester.experiment.dagger2experiment.engine.EngineScope;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class JavaScriptProcessorModule {

//    @Provides
//    @Singleton
//    public JavaScriptProcessor provideJavaScriptProcessor(GameStateProvider gameStateProvider, Scriptable sharedScope) {
//        return new JavaScriptProcessor(gameStateProvider, sharedScope);
//    }

    /*
     * it is enough to provide only the loves level dependencies
     * (ie, there is no need to explicitly list JavaScriptProcessor)
     */

    @Provides
    @Singleton
    public Scriptable provideSharedScope() {
        try {
            return Context.enter().initStandardObjects(null, true);
        } finally {
            Context.exit();
        }
    }

    @Provides
    @Singleton
    public JavaScriptLoader provideJavaScriptLoader(@EngineScope android.content.Context context) {
        return new JavaScriptLoader(context);
    }

}
