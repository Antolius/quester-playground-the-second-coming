package com.quester.experiment.dagger2experiment.engine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;

import com.quester.experiment.dagger2experiment.data.quest.Quest;

import org.parceler.Parcels;

/**
 * Created by Josip on 14/01/2015!
 */
public abstract class GameService extends Service {

    public static final String QUEST_EXTRA_ID = "QUEST_EXTRA_ID";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startGame(getQuestFromIntent(intent));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopGame();

        super.onDestroy();
    }

    protected abstract void stopGame();

    protected abstract void startGame(Quest quest);

    private Quest getQuestFromIntent(Intent intent) {
        Parcelable questParcelable = intent.getParcelableExtra(QUEST_EXTRA_ID);

        try {
            return (Quest) Parcels.unwrap(questParcelable);
        } catch (Exception exception) {
            //TODO: granulate exception handling
            throw new IllegalArgumentException("Provide Quest as parcelable extra in the intent");
        }
    }
}
