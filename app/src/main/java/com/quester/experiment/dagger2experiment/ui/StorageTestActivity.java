package com.quester.experiment.dagger2experiment.ui;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.quester.experiment.dagger2experiment.ActivityInjectionComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.archive.QuestArchiver;
import com.quester.experiment.dagger2experiment.archive.QuestPackage;
import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.archive.QuestGraphConverter;
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

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.widget.AdapterView.OnItemClickListener;

public class StorageTestActivity extends InjectionActivity {

    private QuestArchiver archiver;

    @InjectView(R.id.available_quests)
    protected ListView questsList;

    @Inject
    protected QuestRepository questRepository;

    @Override
    protected void inject(ActivityInjectionComponent activityInjectionComponent) {
        activityInjectionComponent.injectActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_test);

        ButterKnife.inject(this);

        archiver = new QuestArchiver(questRepository, this);

        //find .qst files in external storage
        final List<QuestPackage> questFiles = archiver.scanExternalStorage();
        //create list from .qst file names
        questsList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, R.id.quest_name, getQuestFileNames(questFiles)));
        //unzip the .qst file on select
        questsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                archiver.install(questFiles.get(position));
            }
        });
    }

    private List<String> getQuestFileNames(List<QuestPackage> questFiles) {
        List<String> quests = new ArrayList<>();
        for (QuestPackage questPackage : questFiles) {
            quests.add(questPackage.getName());
        }
        return quests;
    }

}
