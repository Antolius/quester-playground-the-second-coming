package com.quester.experiment.dagger2experiment.archive;

import android.content.Context;

import com.quester.experiment.dagger2experiment.archive.cryptographer.QuestCryptographer;
import com.quester.experiment.dagger2experiment.persistence.quest.QuestRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ArchiverModule {

    @Inject
    public QuestRepository repository;

    private final Context context;

    public ArchiverModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public QuestArchiver provideQuestArchiver() {
        return new QuestArchiver(repository, new QuestStorage(context), new QuestCryptographer());
    }
}
