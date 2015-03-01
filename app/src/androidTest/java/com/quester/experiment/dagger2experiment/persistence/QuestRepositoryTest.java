package com.quester.experiment.dagger2experiment.persistence;

import com.quester.experiment.dagger2experiment.TestUtils;
import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class QuestRepositoryTest {

    private QuestRepository repository;

    private Quest retrievedQuest;
    private List<Quest> retrievedQuests;

    @Before
    public void setUp() {
        repository = new QuestRepository(TestUtils.getDatabase());
    }

    @Test
    public void retrieveQuest() {

        givenPersistedQuest(MockedQuestUtils.mockLinearQuest(0, "Test Quest", 3));

        whenFindOne(1);

        thenQuestIsNamed("Test Quest");
    }

    @Test
    public void retrieveQuestByGlobalId() {

        Quest quest = MockedQuestUtils.mockLinearQuest(0, "one with global id", 3);
        QuestMetaData metaData = new QuestMetaData();
        metaData.setOriginalId(123);
        quest.setQuestMetaData(metaData);
        givenPersistedQuest(quest);

        whenFindOneByGlobalId(123);

        thenQuestIsNamed("one with global id");
    }

    @Test
    public void saveMultipleQuestAndRetrieveAll() {

        givenPersistedQuest(MockedQuestUtils.mockLinearQuest(0, "test1", 1));
        givenPersistedQuest(MockedQuestUtils.mockLinearQuest(0, "test2", 2));
        givenPersistedQuest(MockedQuestUtils.mockLinearQuest(0, "test3", 3));

        whenFindAll();

        thenFoundQuests(3);
    }

    private void givenPersistedQuest(Quest quest) {
        repository.save(quest);
    }

    private void whenFindOne(long id) {
        retrievedQuest = repository.findOne(id);
    }

    private void whenFindOneByGlobalId(long id) {
        retrievedQuest = repository.findOneByGlobalId(id);
    }

    private void whenFindAll() {
        retrievedQuests = repository.findAll();
    }

    private void thenFoundQuests(int numberOfQuests) {
        assertEquals(numberOfQuests, retrievedQuests.size());
    }

    private void thenQuestIsNamed(String name) {
        assertEquals(name, retrievedQuest.getName());
    }
}