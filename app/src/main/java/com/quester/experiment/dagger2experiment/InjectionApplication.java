package com.quester.experiment.dagger2experiment;

import android.app.Application;
import android.util.Log;

/**
 * Created by Josip on 13/01/2015!
 */
public class InjectionApplication extends Application {

    public static final String TAG = "InjectionApplication";

    private ApplicationComponent applicationComponent;

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

    public ApplicationComponent getApplicationComponent() {
        Log.d(TAG, "call to getApplicationComponent, returning " + applicationComponent.getClass().getSimpleName());
        return applicationComponent;
    }
}
