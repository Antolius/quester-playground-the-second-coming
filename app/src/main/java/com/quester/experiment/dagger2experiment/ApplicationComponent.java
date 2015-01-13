package com.quester.experiment.dagger2experiment;

import com.quester.experiment.dagger2experiment.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Josip on 13/01/2015!
 */
@Singleton
@Component(modules = {
        ApplicationModule.class
})
public interface ApplicationComponent {

    void injectApplication(InjectionApplication application);
    void injectActivity(MainActivity activity);

}
