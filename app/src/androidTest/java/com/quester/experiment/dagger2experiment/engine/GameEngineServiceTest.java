package com.quester.experiment.dagger2experiment.engine;

import android.content.Context;

import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.engine.notification.Notifier;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GameEngineServiceTest {

    private GameEngineService service;
    private Notifier notifier = mock(Notifier.class);

    @Before
    public void setUp(){
        service = new GameEngineService();

        final Context context = Robolectric.application;

        EngineComponent engineComponent = Dagger_EngineComponent.builder()
                .engineModule(new EngineModule(service){
                    @Override
                    public Context provideServiceContext() {
                        return context;
                    }
                })
                .notifierModule(new NotifierTestModule(context, notifier))
                .gameStateModule(new GameStateModule(context))
                .build();
        engineComponent.injectGameEngineService(service);
    }

    @Test
    public void test(){

        Quest quest = MockedQuestUtils.mockLinearQuest(1, "test", 2);
        service.startGame(quest);

        verify(notifier, times(2)).notifyCheckpointReached(any(Checkpoint.class));
    }
}
