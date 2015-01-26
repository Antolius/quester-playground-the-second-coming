package com.quester.experiment.dagger2experiment.engine.state;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class GameStateModule {

    private static GameStateProvider gameStateProvider;

    @Provides
    @Singleton
    public GameStateProvider provideGameStateProvider() {
        if (gameStateProvider == null) {
            gameStateProvider = new GameStateProvider();
        }
        return gameStateProvider;
    }

}
