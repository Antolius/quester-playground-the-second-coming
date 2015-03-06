package com.quester.experiment.dagger2experiment.archive;

import com.quester.experiment.dagger2experiment.archive.parser.QuestJsonParser;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.quest.QuestRepository;

import java.util.List;

public class QuestArchiver {

    private QuestRepository repository;
    private QuestStorage storage;
    private QuestJsonParser questJsonParser;

    public QuestArchiver(QuestRepository repository, QuestStorage storage, QuestJsonParser questJsonParser) {
        this.repository = repository;
        this.storage = storage;
        this.questJsonParser = questJsonParser;
    }

    /**
     * returns all installed quests
     * @return list of quests
     */
    public List<Quest> findAllQuests(){

        return repository.findAll();
    }

    /**
     * finds all *.qst files in external storage and returns their quest package representations
     * @return list of quest packages
     */
    public List<QuestPackage> findQuestPackages() {

        return storage.findQuestPackages();
    }

    /**
     * saves a representation of a *.qst file,
     * unpacks it to internal storage and saves in the database repository,
     * or updates a current version
     * @param questPackage a representation od a *.qst file
     */
    public void saveToArchive(QuestPackage questPackage) {

        Quest persisted = repository.findOneByGlobalId(questPackage.getId());
        if (persisted == null) {
            unpackAndSave(questPackage);
            return;
        }

        Quest quest = questJsonParser.parseQuestJson(storage.extractQuestJson(questPackage));

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
                questJsonParser.parseQuestJson(
                        storage.getQuestJson(questPackage)));
    }
}
