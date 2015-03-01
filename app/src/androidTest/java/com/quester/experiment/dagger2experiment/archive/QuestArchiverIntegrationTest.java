package com.quester.experiment.dagger2experiment.archive;

import com.quester.experiment.dagger2experiment.TestUtils;
import com.quester.experiment.dagger2experiment.archive.cryptographer.QuestCryptographer;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.QuestRepository;
import com.sromku.simple.storage.SimpleStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;


@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class QuestArchiverIntegrationTest {

    private QuestRepository repository;

    private QuestArchiver archiver;

    @Before
    public void setUp(){

        repository = new QuestRepository(TestUtils.getDatabase());
        QuestStorage storage = new QuestStorage(Robolectric.application);
        QuestCryptographer cryptographer = new QuestCryptographer();

        archiver = new QuestArchiver(repository, storage, cryptographer);
    }

    @Test
    public void test() throws IOException {

        TestUtils.moveQuestPackageFromAssetsToExternalStorage("123_test.qst");

        List<QuestPackage> questPackages = archiver.findQuestPackages();

        assertEquals(1, questPackages.size());

        archiver.saveToArchive(questPackages.get(0));

        Quest quest = repository.findOneByGlobalId(123);

        assertEquals("test", quest.getName());

        SimpleStorage.getExternalStorage().deleteFile("", "123_test.qst");
    }
}
