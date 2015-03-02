package com.quester.experiment.dagger2experiment.persistence;

import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.game.GameStateRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GameRepositoryTest {

    @Test
    public void test(){

        GameStateRepository repository = new GameStateRepository(Robolectric.application);

        Quest quest = MockedQuestUtils.mockLinearQuest(1, "test", 2);
        GameState state = new GameState(quest.getId(), quest.getQuestGraph(), "{}");
        repository.save(state);
        GameState state1 = repository.findOne(1);
        assertEquals("{}", state1.getPersistentGameObjectAsString());
    }
}
