package com.quester.experiment.dagger2experiment.archive;

import com.quester.experiment.dagger2experiment.archive.cryptographer.QuestCryptographer;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.QuestRepository;

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

    public List<Quest> findAllQuests(){

        return repository.findAll();
    }

    public List<QuestPackage> findQuestPackages() {

        return storage.findQuestPackages();
    }

    public void saveToArchive(QuestPackage questPackage) {

        Quest persisted = repository.findOneByGlobalId(questPackage.getId());
        if (persisted == null) {
            unpackAndSave(questPackage);
            return;
        }

        Quest quest = cryptographer.decryptQuest(
                storage.extractQuestScroll(questPackage));

        if (quest.getQuestMetaData().getVersion() > persisted.getQuestMetaData().getVersion()) {
            unpackAndSave(questPackage);
        }
    }

    private void unpackAndSave(QuestPackage questPackage) {
        storage.storePackage(questPackage);
        saveQuestToPersistence(questPackage);
    }

    private void saveQuestToPersistence(QuestPackage questPackage) {
        repository.save(
                cryptographer.decryptQuest(
                        storage.getQuestScroll(questPackage)));
    }
}
