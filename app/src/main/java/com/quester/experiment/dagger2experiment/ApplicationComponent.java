package com.quester.experiment.dagger2experiment;

import com.quester.experiment.dagger2experiment.engine.state.GameStateModule;
import com.quester.experiment.dagger2experiment.ui.CheckpointReachedActivity;
import com.quester.experiment.dagger2experiment.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Josip on 13/01/2015!
 * Application level injection component.
 * Contained in current @see InjectionApplication instance.
 * Has a separate injectActivity method defined for every activity.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        GameStateModule.class
})
public interface ApplicationComponent {

    void injectApplication(InjectionApplication application);

    void injectActivity(MainActivity activity);

    void injectActivity(CheckpointReachedActivity activity);

    /*
    other injectActivity methods go here
     */
}
