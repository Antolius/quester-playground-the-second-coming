package com.quester.experiment.dagger2experiment.archive;

import android.content.Context;
import android.os.Environment;

import com.bluelinelabs.logansquare.LoganSquare;
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
    private Storage externalStorage;
    private Storage internalStorage;
    private Context context;

    public QuestArchiver(QuestRepository repository, Context context) {
        externalStorage = SimpleStorage.getExternalStorage();
        internalStorage = SimpleStorage.getInternalStorage(context);
        internalStorage.createDirectory("Quests", false);
        this.repository = repository;
        this.context = context;
    }

    public List<QuestPackage> scanExternalStorage(){

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
                result.add(new QuestPackage(Long.valueOf(parts[0]),parts[1],file));
            }
        }

        return result;
    }

    public void install(QuestPackage questPackage) {

        Quest persisted = repository.findByGlobalId(questPackage.getId());
        if(persisted == null){
            unpackAndSave(questPackage);
            return;
        }

        unpack(Environment.getExternalStorageState(), questPackage.getFile());
        Quest quest = extractQuest(externalStorage, questPackage);
        externalStorage.deleteDirectory("Quests/"+questPackage.getDirectoryName());

        if(quest.getQuestMetaData().getVersion() > persisted.getQuestMetaData().getVersion()){
            unpackAndSave(questPackage);
        }

    }

    private void unpackAndSave(QuestPackage questPackage){
        unpack(context.getFilesDir().getPath(), questPackage.getFile());
        repository.save(extractQuest(internalStorage, questPackage));
    }

    private void unpack(String path, File file) {
        try {
            new ZipFile(file).extractAll(path + "/Quests");
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private Quest extractQuest(Storage storage, QuestPackage questPackage){
        String json = storage.readTextFile("Quests/"+questPackage.getDirectoryName(), "quest.json");
        try {
            return LoganSquare.parse(json, Quest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
