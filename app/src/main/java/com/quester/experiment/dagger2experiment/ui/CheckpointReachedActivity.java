package com.quester.experiment.dagger2experiment.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.quester.experiment.dagger2experiment.ActivityInjectionComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.archive.QuestStorage;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateProvider;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.apache.http.protocol.HTTP;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckpointReachedActivity extends InjectionActivity {

    private static final Logger logger = Logger.instance(CheckpointReachedActivity.class);

    @InjectView(R.id.webView)
    protected WebView webView;

    @Inject
    protected GameStateProvider gameStateProvider;

    private QuestStorage storage;

    @Override
    protected void inject(ActivityInjectionComponent activityInjectionComponent) {
        activityInjectionComponent.injectActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = new QuestStorage(this);

        setContentView(R.layout.activity_checkpoint_reached);
        ButterKnife.inject(this);

        logger.verbose("injected dependencies");

        renderCurrentCheckpoint();
    }

    private void renderCurrentCheckpoint() {
        Checkpoint activeCheckpoint = gameStateProvider.getGameState().getActiveCheckpoint();
        setTitle(activeCheckpoint.getName());
        initiateWebView();
        webView.loadData(
                storage.getHtmlViewContent(
                        gameStateProvider.getGameState().getQuest(),
                        activeCheckpoint),
                "text/html",
                HTTP.UTF_8);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initiateWebView() {
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterfaceWrapper(gameStateProvider.getGameState()), "gameState");
    }
}
