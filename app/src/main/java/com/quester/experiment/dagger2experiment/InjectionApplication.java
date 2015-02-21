package com.quester.experiment.dagger2experiment;

import android.app.Application;
import android.util.Log;

import com.quester.experiment.dagger2experiment.persistence.module.DatabaseModule;

/**
 * Created by Josip on 13/01/2015!
 * @see android.app.Application class used for the app.
 * Contains @see ApplicationComponent instance and exposes it to activities and others.
 */
public class InjectionApplication extends Application {

    public static final String TAG = "InjectionApplication";

    private ActivityInjectionComponent activityInjectionComponent;

    /**
     * This method will build instance of @see ActivityInjectionComponent and save the reference to it.
     * That reference is provided to activities that use it to register for dependency injection.
     * Furthermore, injectApplication is called on that instance so that this class too can use injection.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        activityInjectionComponent = Dagger_ActivityInjectionComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
                .build();

        Log.d(TAG, "built " + activityInjectionComponent.getClass().getSimpleName());

        activityInjectionComponent.injectApplication(this);

        Log.d(TAG, "injected this into " + activityInjectionComponent.getClass().getSimpleName());
    }

    /**
     * @see ActivityInjectionComponent instance used to inject dependencies.
     * @return
     */
    public ActivityInjectionComponent getActivityInjectionComponent() {
        Log.d(TAG, "call to getApplicationComponent, returning " + activityInjectionComponent.getClass().getSimpleName());
        return activityInjectionComponent;
    }
}
