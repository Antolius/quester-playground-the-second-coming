package com.quester.experiment.dagger2experiment.archive;

import android.content.Context;
import android.os.Environment;

import com.bluelinelabs.logansquare.LoganSquare;
import com.quester.experiment.dagger2experiment.archive.cryptographer.QuestCryptographer;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.QuestRepository;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestArchiver {

    private QuestRepository repository;
    private QuestStorage storage;
    private QuestCryptographer cryptographer;

    public QuestArchiver(QuestRepository repository, QuestStorage storage, QuestCryptographer cryptographer) {
        this.repository = repository;
        this.storage = storage;
        this.cryptographer = cryptographer;
    }

    public List<QuestPackage> findQuestPackages() {

        return storage.findQuestPackages();
    }

    public void saveToArchive(QuestPackage questPackage) {

        Quest persisted = repository.findByGlobalId(questPackage.getId());
        if (persisted == null) {
            repository.save(
                    cryptographer.decryptQuest(
                            storage.storePackageAndRetrieveScroll(questPackage)));
            return;
        }

        Quest quest = cryptographer.decryptQuest(
                storage.retrieveScroll(questPackage));

        if (quest.getQuestMetaData().getVersion() > persisted.getQuestMetaData().getVersion()) {
            repository.save(
                    cryptographer.decryptQuest(
                            storage.storePackageAndRetrieveScroll(questPackage)));
        }
    }
}
