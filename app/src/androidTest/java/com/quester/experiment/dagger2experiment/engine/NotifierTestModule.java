package com.quester.experiment.dagger2experiment.engine;

import android.content.Context;

import com.quester.experiment.dagger2experiment.engine.notification.Notifier;
import com.quester.experiment.dagger2experiment.engine.notification.NotifierModule;

import dagger.Module;

@Module
public class NotifierTestModule extends NotifierModule {

    private Notifier notifier;

    public NotifierTestModule(Context context, Notifier notifier) {
        super(context);
        this.notifier = notifier;
    }

    @Override
    public Notifier provideNotifier() {
        return notifier;
    }

}
