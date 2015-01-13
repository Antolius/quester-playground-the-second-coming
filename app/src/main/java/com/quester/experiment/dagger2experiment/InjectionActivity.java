package com.quester.experiment.dagger2experiment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by Josip on 13/01/2015!
 * Injection related abstract Activity class that is to be extended by all activities.
 * Potentially other abstract classes should be created for activities without action bar.
 * If an activity does not extend from this class it should replicate it's functionality.
 */
public abstract class InjectionActivity extends ActionBarActivity {

    public static final String TAG = "InjectionActivity";

    /**
     * To be implemented in extending class by calling applicationComponent.injectActivity(this)
     * This method is created so that proper injectActivity may be called without every activity
     * having to acquire instance of @see ApplicationComponent on their own.
     *
     * @param applicationComponent
     */
    protected abstract void inject(ApplicationComponent applicationComponent);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handelInjection();
    }

    private void handelInjection() {
        ApplicationComponent applicationComponent = ((InjectionApplication) getApplication()).getApplicationComponent();
        this.inject(applicationComponent);

        Log.d(TAG, "injected " + this.getClass().getSimpleName() + " into " + applicationComponent.getClass().getSimpleName());
    }
}
