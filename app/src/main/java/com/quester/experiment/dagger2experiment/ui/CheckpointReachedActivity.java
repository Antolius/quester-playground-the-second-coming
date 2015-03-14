package com.quester.experiment.dagger2experiment.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.quester.experiment.dagger2experiment.ActivityInjectionComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.archive.QuestStorage;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateProvider;
import com.sromku.simple.storage.SimpleStorage;

import org.apache.http.protocol.HTTP;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckpointReachedActivity extends InjectionActivity {

    @InjectView(R.id.webView)
    protected WebView webView;

    @Inject
    protected GameStateProvider gameStateProvider;

    private GameState game;

    @Override
    protected void inject(ActivityInjectionComponent activityInjectionComponent) {
        activityInjectionComponent.injectActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

        renderCurrentCheckpoint();
    }

    private void initialize() {
        setContentView(R.layout.activity_checkpoint_reached);
        ButterKnife.inject(this);

        game = gameStateProvider.getGameState();
        initiateWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initiateWebView() {

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);

        webView.addJavascriptInterface(new JavaScriptInterfaceWrapper(game), "gameState");
    }

    private void renderCurrentCheckpoint() {

        Checkpoint checkpoint = game.getActiveCheckpoint();
        setTitle(checkpoint.getName());

        webView.loadDataWithBaseURL(getBaseUrl(), getHtml(checkpoint),
                "text/html", HTTP.UTF_8, null);
    }

    private String getBaseUrl() {
        return "file://" +
                SimpleStorage.getInternalStorage(this)
                        .getFile("Quests")
                        .getAbsolutePath() + "/";
    }

    private String getHtml(Checkpoint checkpoint) {
        return new QuestStorage(this)
                .getHtmlViewContent(game.getQuest(), checkpoint);
    }

}
