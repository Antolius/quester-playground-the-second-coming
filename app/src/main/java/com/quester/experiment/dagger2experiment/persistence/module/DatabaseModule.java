package com.quester.experiment.dagger2experiment.persistence.module;

import android.util.Log;

import com.quester.experiment.dagger2experiment.InjectionApplication;
import com.quester.experiment.dagger2experiment.persistence.QuestRepository;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final Database database;

    public DatabaseModule(InjectionApplication application) {
        this.database = new Database(application.openOrCreateDatabase("test", 0, null));
    }

    @Provides
    @Singleton
    public QuestRepository provideQuestRepository() {
        return new QuestRepository(database);
    }
}
