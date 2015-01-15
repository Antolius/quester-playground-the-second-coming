package com.quester.experiment.dagger2experiment.engine.state;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class GameStateModule {

    @Provides
    @Singleton
    public GameStateProvider provideGameStateProvider() {
        return new GameStateProvider();
    }

}
