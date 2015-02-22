package com.quester.experiment.dagger2experiment.persistence.module;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.quester.experiment.dagger2experiment.InjectionApplication;
import com.quester.experiment.dagger2experiment.persistence.QuestRepository;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Database;

import org.flywaydb.core.Flyway;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final Database database;

    public DatabaseModule(InjectionApplication application) {

        SQLiteDatabase sqLiteDatabase = application.openOrCreateDatabase("test", 0, null);

        //Flyway flyway = new Flyway();
        //flyway.setDataSource("jdbc:sqlite:" + sqLiteDatabase.getPath(), "", "");
        //flyway.migrate();

        this.database = new Database(sqLiteDatabase);
    }

    @Provides
    @Singleton
    public QuestRepository provideQuestRepository() {
        return new QuestRepository(database);
    }
}
