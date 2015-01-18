package com.quester.experiment.dagger2experiment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.util.Log;

import com.quester.experiment.dagger2experiment.repository.QuestRepository;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.android.ContextHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 13/01/2015!
 * Application level injection module.
 * Contains providers for all injectable resources.
 */
@Module
public class ApplicationModule {

    public static final String TAG = "ApplicationModule";

    private final InjectionApplication application;

    /**
     * @param application current @see Application instance is used for instantiation of application dependent android services.
     */
    public ApplicationModule(InjectionApplication application) {
        Log.d(TAG, "constructor called with " + application.getClass().getSimpleName());
        this.application = application;
    }

    /**
     * @return
     * @see android.content.Context provider which provides current application context.
     * Used for demonstrating use of @see javax.inject.Qualifier.
     */
    @Provides
    @Singleton
    @ApplicationScope
    public Context provideApplicationContext() {
        Log.d(TAG, "call to provideApplicationContext, returning " + application.getClass().getSimpleName());
        return application;
    }

    /**
     * @return
     * @see LocationManager provider used for demo purposis.
     */
    @Provides
    @Singleton
    public LocationManager provideLocationManager() {
        LocationManager locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "call to provideLocationManager, returning " + locationManager.getClass().getSimpleName());
        return locationManager;
    }

    /**
     *
     */
    @Provides
    @Singleton
    public QuestRepository provideQuestRepository(){

        SQLiteDatabase db = application.openOrCreateDatabase("questerDb", 0, null);

        migrateDatabase(db);

        return new QuestRepository(db);
    }

    private void migrateDatabase(SQLiteDatabase db) {
        ContextHolder.setContext(application);
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:sqlite:" + db.getPath(), "", "");
        flyway.setInitOnMigrate(true);
        flyway.migrate();
    }
}
