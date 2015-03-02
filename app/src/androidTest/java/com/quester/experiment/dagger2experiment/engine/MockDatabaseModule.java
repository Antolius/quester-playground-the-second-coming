package com.quester.experiment.dagger2experiment.engine;

import android.content.Context;

import com.quester.experiment.dagger2experiment.persistence.game.GameStateRepository;
import com.quester.experiment.dagger2experiment.persistence.module.DatabaseModule;
import com.quester.experiment.dagger2experiment.persistence.quest.QuestRepository;

import dagger.Module;

import static org.mockito.Mockito.mock;

@Module
public class MockDatabaseModule extends DatabaseModule {

    public MockDatabaseModule(Context context) {
        super(context);
    }

    @Override
    public GameStateRepository provideGameStateRepository() {
        return mock(GameStateRepository.class);
    }

    @Override
    public QuestRepository provideQuestRepository() {
        return mock(QuestRepository.class);
    }
}
