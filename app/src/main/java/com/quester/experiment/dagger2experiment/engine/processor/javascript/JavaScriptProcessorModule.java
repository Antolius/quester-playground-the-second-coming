package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class JavaScriptProcessorModule {

    @Provides
    @Singleton
    public JavaScriptProcessor provideJavaScriptProcessor() {
        return new JavaScriptProcessor();

    }

}
