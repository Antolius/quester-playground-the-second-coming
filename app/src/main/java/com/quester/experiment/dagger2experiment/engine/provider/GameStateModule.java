package com.quester.experiment.dagger2experiment.engine.provider;

import android.content.Context;

import com.quester.experiment.dagger2experiment.persistence.game.GameStateRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Josip on 16/01/2015!
 */
@Module
public class GameStateModule {

    @Inject
    public GameStateRepository gameStateRepository;

    private static GameStateProvider gameStateProvider;
    private Context context;

    public GameStateModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public GameStateProvider provideGameStateProvider() {
        if (gameStateProvider == null) {
            gameStateProvider = new GameStateProvider(new GameStateRepository(context));
        }
        return gameStateProvider;
    }

}
