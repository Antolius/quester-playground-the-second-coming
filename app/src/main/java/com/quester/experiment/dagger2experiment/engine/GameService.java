package com.quester.experiment.dagger2experiment.engine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.parceler.Parcels;

/**
 * Created by Josip on 14/01/2015!
 */
public abstract class GameService extends Service {

    public static final String TAG = "GameService";

    public static final String QUEST_EXTRA_ID = "QUEST_EXTRA_ID";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.v(TAG, "onStartCommand called");

        startGame(extractQuestFromIntent(intent));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.v(TAG, "onDestroy called");

        stopGame();
        super.onDestroy();
    }

    protected abstract void stopGame();

    protected abstract void startGame(Quest quest);

    private Quest extractQuestFromIntent(Intent intent) {
        try {
            return (Quest) Parcels.unwrap(intent.getParcelableExtra(QUEST_EXTRA_ID));
        } catch (Exception exception) {
            //TODO: granulate exception handling
            throw new IllegalArgumentException("Missing Quest as parcelable extra in the intent");
        }
    }
}
