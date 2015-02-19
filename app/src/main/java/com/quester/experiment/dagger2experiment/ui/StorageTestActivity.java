package com.quester.experiment.dagger2experiment.ui;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quester.experiment.dagger2experiment.R;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.widget.AdapterView.OnItemClickListener;

public class StorageTestActivity extends ActionBarActivity {

    @InjectView(R.id.available_quests)
    protected ListView questsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_test);

        ButterKnife.inject(this);

        //find .qst files in external storage
        final List<File> questFiles = findQuestFiles(SimpleStorage.getExternalStorage(), "");
        //create list from .qst file names
        questsList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, R.id.quest_name, getQuestFileNames(questFiles)));
        //unzip the .qst file on select
        questsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            unzipQuestFile(questFiles.get(position));
            }
        });
    }

    private void unzipQuestFile(File file) {
        try {
            new ZipFile(file).extractAll(Environment.getExternalStorageDirectory().getPath() + "/Quests");
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private List<String> getQuestFileNames(List<File> questFiles) {
        List<String> quests = new ArrayList<>();
        for (File file : questFiles) {
            quests.add(file.getName().split("\\.")[0]);
        }
        return quests;
    }

    private List<File> findQuestFiles(Storage storage, String directory) {

        List<File> result = new ArrayList<>();

        for (File file : storage.getFiles(directory, OrderType.NAME)) {
            if (file.isDirectory() && !file.isHidden()) {

                String location;
                if (directory.isEmpty()) {
                    location = file.getName();
                } else {
                    location = directory + "/" + file.getName();
                }
                result.addAll(findQuestFiles(storage, location));
                continue;
            }
            if (file.getName().endsWith(".qst")) {
                result.add(file);
            }
        }

        return result;
    }

}
