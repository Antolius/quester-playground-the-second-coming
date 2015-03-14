package com.quester.experiment.dagger2experiment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quester.experiment.dagger2experiment.ActivityInjectionComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.engine.GameEngineService;
import com.quester.experiment.dagger2experiment.engine.GameService;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StartQuestActivity extends InjectionActivity {

    @InjectView(R.id.quest_name)
    protected TextView questNameText;

    private Quest quest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quest);
        ButterKnife.inject(this);

        quest = Parcels.unwrap(getIntent().getParcelableExtra("quest"));

        questNameText.setText(quest.getName());
    }

    @OnClick(R.id.start_quest_button)
    public void startService() {
        startService(new Intent(this, GameEngineService.class)
                .putExtra(GameService.QUEST_EXTRA_ID, Parcels.wrap(quest)));
    }

    @OnClick(R.id.stop_quest_button)
    public void stopService() {
        stopService(new Intent(this, GameEngineService.class));
    }

    @Override
    protected void inject(ActivityInjectionComponent activityInjectionComponent) {
        activityInjectionComponent.injectActivity(this);
    }
}
