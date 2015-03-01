package com.quester.experiment.dagger2experiment.archive;

import com.quester.experiment.dagger2experiment.archive.cryptographer.QuestCryptographer;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;
import com.quester.experiment.dagger2experiment.persistence.QuestRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class QuestArchiverTest {


    private QuestRepository repository = mock(QuestRepository.class);
    private QuestStorage storage = mock(QuestStorage.class);
    private QuestCryptographer cryptographer = mock(QuestCryptographer.class);

    private QuestArchiver archiver;

    @Before
    public void setUp(){

        archiver = new QuestArchiver(repository, storage, cryptographer);
    }

    @Test
    public void questIsArchivedIfDoesNotExist(){

        QuestPackage questPackage = new QuestPackage(1L,"123", new File("test"));
        Quest quest = new Quest();

        when(repository.findOneByGlobalId(1L)).thenReturn(null);
        when(storage.getQuestScroll(questPackage)).thenReturn("encrypted");
        when(cryptographer.decryptQuest("encrypted")).thenReturn(quest);

        archiver.saveToArchive(questPackage);

        verify(repository).save(quest);
    }

    @Test
    public void questIsArchivedIfNewVersionIsHigher(){

        QuestPackage questPackage = new QuestPackage(1L,"123", new File("test"));

        Quest persisted = new Quest();
        QuestMetaData persistedMetaData = new QuestMetaData();
        persistedMetaData.setVersion(1);
        persisted.setQuestMetaData(persistedMetaData);

        Quest fromPackage = new Quest();
        QuestMetaData fromPackageMetaData = new QuestMetaData();
        fromPackageMetaData.setVersion(2);
        fromPackage.setQuestMetaData(fromPackageMetaData);

        when(repository.findOneByGlobalId(1L)).thenReturn(persisted);

        when(storage.extractQuestScroll(questPackage)).thenReturn("encrypted");
        when(storage.getQuestScroll(questPackage)).thenReturn("encrypted");
        when(cryptographer.decryptQuest("encrypted")).thenReturn(fromPackage);

        archiver.saveToArchive(questPackage);

        verify(repository).save(fromPackage);
    }

    @Test
    public void questIsNotArchivedIfNewVersionIsLower(){

        QuestPackage questPackage = new QuestPackage(1L,"123", new File("test"));

        Quest persisted = new Quest();
        QuestMetaData persistedMetaData = new QuestMetaData();
        persistedMetaData.setVersion(2);
        persisted.setQuestMetaData(persistedMetaData);

        Quest fromPackage = new Quest();
        QuestMetaData fromPackageMetaData = new QuestMetaData();
        fromPackageMetaData.setVersion(1);
        fromPackage.setQuestMetaData(fromPackageMetaData);

        when(repository.findOneByGlobalId(1L)).thenReturn(persisted);

        when(storage.extractQuestScroll(questPackage)).thenReturn("encrypted");
        when(cryptographer.decryptQuest("encrypted")).thenReturn(fromPackage);

        archiver.saveToArchive(questPackage);

        verify(repository, never()).save(any(Quest.class));
    }
}
