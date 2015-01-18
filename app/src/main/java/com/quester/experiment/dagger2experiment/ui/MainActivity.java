package com.quester.experiment.dagger2experiment.ui;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.quester.experiment.dagger2experiment.ApplicationComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.engine.GameEngineService;
import com.quester.experiment.dagger2experiment.repository.QuestRepository;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends InjectionActivity {

    private Parcelable wrappedQuest;

    @InjectView(R.id.text_view)
    TextView infoTextVeiw;

    private static final String TAG = "MainActivity";

    /**
     * LocationManager is injected for demo purposes.
     */
    @Inject
    protected LocationManager locationManager;

    @Inject
    protected QuestRepository questRepository;

    /**
     * "Default" implementation of @see InjectionActivity#inject
     *
     * @param applicationComponent
     */
    @Override
    protected void inject(ApplicationComponent applicationComponent) {
        applicationComponent.injectActivity(this);
    }

    /**
     * Calling super.onCreate will trigger dependency injection that will in turn use @see MainActivity#inject method.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "injected dependencies");
        Log.d(TAG, String.valueOf(locationManager != null));

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Quest mockedQuest = questRepository.queryQuest(1);
        if(mockedQuest == null) {
            mockedQuest = MockedQuestUtils.mockLinearQuest(1, "Mocked linear quest", 3);
            questRepository.persistQuest(mockedQuest);
        }

        wrappedQuest = Parcels.wrap(mockedQuest);

        infoTextVeiw.setText(mockedQuest.toString());
    }

    @OnClick(R.id.start_button)
    public void starService() {
        startService(new Intent(this, GameEngineService.class).putExtra(GameEngineService.QUEST_EXTRA_ID, wrappedQuest));
    }

    @OnClick(R.id.stop_button)
    public void stoService() {
        stopService(new Intent(this, GameEngineService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
