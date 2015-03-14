package com.quester.experiment.dagger2experiment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quester.experiment.dagger2experiment.ActivityInjectionComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.persistence.quest.QuestRepository;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.widget.AdapterView.*;

public class ChooseQuestActivity extends InjectionActivity {

    @Inject
    protected QuestRepository questRepository;

    @InjectView(R.id.saved_quests)
    protected ListView questsList;

    private Map<String, Quest> questMap = new HashMap<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        final List<String> questNames = getQuestFileNames();

        questsList.setAdapter(getAdapter(questNames));

        questsList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoStartQuestActivity(questNames.get(position));
            }
        });
    }

    private void initialize() {
        context = this;
        setContentView(R.layout.activity_choose_quest);
        ButterKnife.inject(this);

        //temporary hack
        if(questRepository.findAll().isEmpty()){
            questRepository.save(MockedQuestUtils.mockLinearQuest(1,"Linear quest",3));
        }

    }

    private List<String> getQuestFileNames() {
        List<String> questNames = new ArrayList<>();
        for (Quest quest : questRepository.findAll()) {
            questNames.add(quest.getName());
            questMap.put(quest.getName(), quest);
        }
        return questNames;
    }

    private void gotoStartQuestActivity(String questName) {
        Intent intent = new Intent(context, StartQuestActivity.class);
        intent.putExtra("quest", Parcels.wrap(questMap.get(questName)));
        startActivity(intent);
    }

    private ArrayAdapter<String> getAdapter(List<String> questNames) {
        return new ArrayAdapter<>(this, R.layout.list_item, R.id.quest_name, questNames);
    }

    @Override
    protected void inject(ActivityInjectionComponent activityInjectionComponent) {
        activityInjectionComponent.injectActivity(this);
    }
}
