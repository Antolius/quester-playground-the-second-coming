package com.quester.experiment.dagger2experiment.archive.cryptographer;

import com.bluelinelabs.logansquare.LoganSquare;
import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.quest.Quest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class QuestCryptographerTest {

    @Test
    public void questIsEncryptedCorrectly() throws IOException {

        Quest quest = MockedQuestUtils.mockLinearQuest(1, "test", 2);
        String json = LoganSquare.serialize(quest);

        assertEquals("{\"name\":\"test\"," +
                        "\"questGraph\":{" +
                        "\"checkpoints\":[" +
                        "{\"id\":0,\"name\":\"Checkpoint #0\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}," +
                        "{\"id\":1,\"name\":\"Checkpoint #1\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}]," +
                        "\"links\":{\"0\":[1],\"1\":[]}}," +
                        "\"id\":1," +
                        "\"questMetaData\":{\"originalId\":0,\"version\":0}}",
                json);
    }

    @Test
    public void questIsDecryptedCorrectly() throws IOException {

        Quest quest = LoganSquare.parse(
                "{\"name\":\"test\"," +
                        "\"questGraph\":{" +
                        "\"checkpoints\":[" +
                        "{\"id\":0,\"name\":\"Checkpoint #0\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}," +
                        "{\"id\":1,\"name\":\"Checkpoint #1\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}]," +
                        "\"links\":{\"0\":[1],\"1\":[]}}," +
                        "\"id\":1," +
                        "\"questMetaData\":{\"originalId\":0,\"version\":0}}",
                Quest.class);

        assertEquals("test", quest.getName());
        assertEquals(1L, quest.getId());
        assertEquals(0L, quest.getQuestMetaData().getOriginalId());
        assertEquals(0L, quest.getQuestMetaData().getVersion());
        assertEquals(2, quest.getQuestGraph().getAllCheckpoints().size());
    }
}
