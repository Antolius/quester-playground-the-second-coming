package com.quester.experiment.dagger2experiment.persistence.module;

import android.database.sqlite.SQLiteDatabase;

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

        //should enable in production or move somewhere
        //migrate(sqLiteDatabase);

        this.database = new Database(sqLiteDatabase);
    }

    public void migrate(SQLiteDatabase database){
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:sqlite:" + database.getPath(), "", "");
        flyway.migrate();
    }

    @Provides
    @Singleton
    public QuestRepository provideQuestRepository() {
        return new QuestRepository(database);
    }
}
