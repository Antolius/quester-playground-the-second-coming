package com.quester.experiment.dagger2experiment;

import android.app.Application;
import android.util.Log;

/**
 * Created by Josip on 13/01/2015!
 * @see android.app.Application class used for the app.
 * Contains @see ApplicationComponent instance and exposes it to activities and others.
 */
public class InjectionApplication extends Application {

    public static final String TAG = "InjectionApplication";

    private ApplicationComponent applicationComponent;

    /**
     * This method will build instance of @see ApplicationComponent and save the reference to it.
     * That reference is provided to activities that use it to register for dependency injection.
     * Furthermore, injectApplication is called on that instance so that this class too can use injection.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        applicationComponent = Dagger_ApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        Log.d(TAG, "built " + applicationComponent.getClass().getSimpleName());

        applicationComponent.injectApplication(this);

        Log.d(TAG, "injected this into " + applicationComponent.getClass().getSimpleName());
    }

    /**
     * @see com.quester.experiment.dagger2experiment.ApplicationComponent instance used to inject dependencies.
     * @return
     */
    public ApplicationComponent getApplicationComponent() {
        Log.d(TAG, "call to getApplicationComponent, returning " + applicationComponent.getClass().getSimpleName());
        return applicationComponent;
    }
}
