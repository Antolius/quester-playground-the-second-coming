package com.quester.experiment.dagger2experiment.ui;

import android.os.Bundle;
import android.webkit.WebView;

import com.quester.experiment.dagger2experiment.ApplicationComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.state.GameStateProvider;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.apache.http.protocol.HTTP;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.quester.experiment.dagger2experiment.util.FileLoader.readFile;

/**
 * Created by Josip on 25/01/2015!
 */
public class CheckpointReachedActivity extends InjectionActivity {

    public static final String TAG = "CheckpointReachedActivity";

    @InjectView(R.id.webView)
    protected WebView webView;

    @Inject
    protected GameStateProvider gameStateProvider;

    @Override
    protected void inject(ApplicationComponent applicationComponent) {
        applicationComponent.injectActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkpoint_reached);
        ButterKnife.inject(this);

        Logger.v(TAG, "injected dependencies");

        Checkpoint activeCheckpoint = gameStateProvider.getGameState().getActiveCheckpoint();
        setTitle(activeCheckpoint.getName());

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterfaceWrapper(gameStateProvider.getGameState()), "gameState");

        webView.loadData(readFile(activeCheckpoint.getViewHtmlFileName(), this), "text/html", HTTP.UTF_8);

    }
}