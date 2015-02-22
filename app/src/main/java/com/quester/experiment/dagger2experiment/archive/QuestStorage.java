package com.quester.experiment.dagger2experiment.archive;

import android.content.Context;
import android.os.Environment;

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

    public String storePackageAndRetrieveScroll(QuestPackage questPackage) {

        unpack(questPackage, internalStorage);

        return getQuestScroll(questPackage, internalStorage);
    }

    public String retrieveScroll(QuestPackage questPackage) {

        unpack(questPackage, externalStorage);

        String questScroll = getQuestScroll(questPackage, externalStorage);
        externalStorage.deleteDirectory("Quests/" + questPackage.getDirectoryName());
        return questScroll;
    }

    private void unpack(QuestPackage questPackage, Storage storage){
        try {
            new ZipFile(questPackage.getFile()).extractAll(storage.getFile("Quests").getPath());
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private String getQuestScroll(QuestPackage questPackage, Storage storage){

        return storage.readTextFile("Quests/" + questPackage.getDirectoryName(), "quest.json");
    }

    public void removeQuest(QuestPackage questPackage) {

        internalStorage.deleteDirectory("Quests/" + questPackage.getDirectoryName());
    }
}
