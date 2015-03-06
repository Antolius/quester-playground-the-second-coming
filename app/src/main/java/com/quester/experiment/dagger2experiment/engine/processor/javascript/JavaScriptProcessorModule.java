package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import com.quester.experiment.dagger2experiment.archive.QuestStorage;
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

    private static final boolean ARE_OBJECTS_FINAL = true;

    @Provides
    @Singleton
    public Scriptable provideSharedScope() {
        try {
            return Context.enter().initStandardObjects(null, ARE_OBJECTS_FINAL);
        } finally {
            Context.exit();
        }
    }

    @Provides
    @Singleton
    public QuestStorage provideQuestStorage(@EngineScope android.content.Context context) {
        return new QuestStorage(context);
    }

}
