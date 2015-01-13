package com.quester.experiment.dagger2experiment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by Josip on 13/01/2015!
 */
public abstract class InjectionActivity extends ActionBarActivity {

    public static final String TAG = "InjectionActivity";

    /**
     * to be implemented by calling applicationComponent.injectActivity(this) by implementing class
     * @param applicationComponent
     */
    protected abstract void inject(ApplicationComponent applicationComponent);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "call to onCreate");

        ApplicationComponent applicationComponent = ((InjectionApplication)getApplication()).getApplicationComponent();
        this.inject(applicationComponent);

        Log.d(TAG, "injected " + this.getClass().getSimpleName() + " into " + applicationComponent.getClass().getSimpleName());
    }
}
