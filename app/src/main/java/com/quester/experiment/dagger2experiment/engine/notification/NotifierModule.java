package com.quester.experiment.dagger2experiment.engine.notification;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NotifierModule {

    private Context context;

    public NotifierModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Notifier provideNotifier(){
        return new Notifier(context);
    }

}
