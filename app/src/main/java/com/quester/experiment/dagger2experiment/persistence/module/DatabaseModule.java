package com.quester.experiment.dagger2experiment.persistence.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.quester.experiment.dagger2experiment.InjectionApplication;
import com.quester.experiment.dagger2experiment.persistence.game.GameStateRepository;
import com.quester.experiment.dagger2experiment.persistence.quest.QuestRepository;
import com.quester.experiment.dagger2experiment.persistence.wrapper.sql.Database;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.android.ContextHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private Database database;
    private Context context;

    public DatabaseModule(Context context) {

        this.context = context;
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("test", 0, null);

        //should enable in production or move somewhere
        migrate(sqLiteDatabase);

        this.database = new Database(sqLiteDatabase);
    }

    public void migrate(SQLiteDatabase database){

        ContextHolder.setContext(context);
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:sqlite:" + database.getPath(), "", "");
        flyway.migrate();
    }

    @Provides
    @Singleton
    public QuestRepository provideQuestRepository() {
        return new QuestRepository(database);
    }

    @Provides
    @Singleton
    public GameStateRepository provideGameStateRepository() {
        return new GameStateRepository(context);
    }
}
