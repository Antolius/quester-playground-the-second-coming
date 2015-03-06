package com.quester.experiment.dagger2experiment.archive;

import android.content.Context;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuestStorage {

    private Storage externalStorage;
    private Storage internalStorage;

    public QuestStorage(Context context) {
        externalStorage = SimpleStorage.getExternalStorage();
        internalStorage = SimpleStorage.getInternalStorage(context);
        internalStorage.createDirectory("Quests", false);
        externalStorage.createDirectory("Quests", false);
    }

    /**
     * finds all *.qst files in external storage and returns their quest package representations
     * @return list of quest packages
     */
    public List<QuestPackage> findQuestPackages() {

        return findQuestFiles("");
    }

    private List<QuestPackage> findQuestFiles(String directory) {

        List<QuestPackage> result = new ArrayList<>();

        for (File file : externalStorage.getFiles(directory, OrderType.NAME)) {
            if (file.isDirectory() && !file.isHidden()) {

                String location;
                if (directory.isEmpty()) {
                    location = file.getName();
                } else {
                    location = directory + "/" + file.getName();
                }
                result.addAll(findQuestFiles(location));
                continue;
            }
            if (file.getName().endsWith(".qst")) {
                String[] parts = file.getName().split("\\.|_");
                result.add(new QuestPackage(Long.valueOf(parts[0]), parts[1], file));
            }
        }

        return result;
    }

    /**
     * saves a representation of a *.qst file
     * by unpacking it to internal storage
     * @param questPackage a representation od a *.qst file
     */
    public void storePackage(QuestPackage questPackage) {

        unpack(questPackage, internalStorage);
    }

    /**
     * retrieves content of quest.json file inside the quest package in internal storage
     *
     * @param questPackage quest package descriptor
     * @return json from quest.json file inside the package
     */
    public String getQuestJson(QuestPackage questPackage) {

        return getQuestJson(questPackage, internalStorage);
    }

    /**
     * retrieves content of a javascript file inside the quest for a checkpoint in internal storage
     *
     * @param quest      quest to search in internal storage
     * @param checkpoint checkpoint to determine which script to run
     * @return string from *.js file inside the quest scripts directory
     */
    public String getScriptContent(Quest quest, Checkpoint checkpoint) {

        return internalStorage.readTextFile(
                "Quests/" + quest.getDirectoryName() + "/scripts",
                checkpoint.getEventsScriptFileName() + ".js");
    }

    /**
     * checks if a javascript file inside the quest for a checkpoint in internal storage exists
     *
     * @param quest      quest to search in internal storage
     * @param checkpoint checkpoint to determine which script to run
     * @return true if script exists false instead
     */
    public boolean hasScript(Quest quest, Checkpoint checkpoint) {

        return checkpoint.getEventsScriptFileName() != null
                && internalStorage.isFileExist("Quests/"
                + quest.getQuestMetaData().getOriginalId()
                + "_" + quest.getName() + "/scripts",
                checkpoint.getEventsScriptFileName() + ".js");

    }

    /**
     * retrieves content of a html file inside the quest for a checkpoint in internal storage
     *
     * @param quest      quest to search in internal storage
     * @param checkpoint checkpoint to determine which html view to return
     * @return string from *.js file inside the quest scripts directory
     */
    public String getHtmlViewContent(Quest quest, Checkpoint checkpoint) {

        return internalStorage.readTextFile(
                "Quests/" + quest.getDirectoryName() + "/html",
                checkpoint.getViewHtmlFileName() + ".html");
    }

    /**
     * unpacks questPackage temporarily to Quests directory in external storage
     * to read the content of quest.json
     *
     * @param questPackage quest package descriptor
     * @return json from quest.json field inside the package
     */
    public String extractQuestJson(QuestPackage questPackage) {

        unpack(questPackage, externalStorage);

        String questJson = getQuestJson(questPackage, externalStorage);
        externalStorage.deleteDirectory("Quests/" + questPackage.getDirectoryName());
        return questJson;
    }

    private void unpack(QuestPackage questPackage, Storage storage) {
        try {
            new ZipFile(questPackage.getFile()).extractAll(storage.getFile("Quests").getPath());
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private String getQuestJson(QuestPackage questPackage, Storage storage) {

        return storage.readTextFile("Quests/" + questPackage.getDirectoryName(), "quest.json");
    }

    /**
     * quest folder and sub data are removed
     *
     * @param questPackage should be changed to simpler
     */
    public void removeQuest(QuestPackage questPackage) {

        internalStorage.deleteDirectory("Quests/" + questPackage.getDirectoryName());
    }
}
