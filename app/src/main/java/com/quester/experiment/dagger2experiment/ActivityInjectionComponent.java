package com.quester.experiment.dagger2experiment;

import com.quester.experiment.dagger2experiment.archive.ArchiverModule;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateModule;
import com.quester.experiment.dagger2experiment.persistence.module.DatabaseModule;
import com.quester.experiment.dagger2experiment.ui.CheckpointReachedActivity;
import com.quester.experiment.dagger2experiment.ui.MainActivity;
import com.quester.experiment.dagger2experiment.ui.StorageTestActivity;
import com.quester.experiment.dagger2experiment.ui.WebViewExampleActivity;

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
        GameStateModule.class,
        ArchiverModule.class,
        DatabaseModule.class,
})
public interface ActivityInjectionComponent {

    void injectApplication(InjectionApplication application);

    void injectActivity(MainActivity activity);

    void injectActivity(StorageTestActivity activity);

    void injectActivity(CheckpointReachedActivity activity);

    void injectActivity(WebViewExampleActivity webViewExampleActivity);

    /*
    other injectActivity methods go here
     */
}
