package com.quester.experiment.dagger2experiment.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.quester.experiment.dagger2experiment.ActivityInjectionComponent;
import com.quester.experiment.dagger2experiment.InjectionActivity;
import com.quester.experiment.dagger2experiment.R;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WebViewExampleActivity extends InjectionActivity {

    @InjectView(R.id.templateHolder)
    protected WebView templateHolder;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_example);
        ButterKnife.inject(this);

        templateHolder.getSettings().setDomStorageEnabled(true);
        templateHolder.getSettings().setJavaScriptEnabled(true);
        templateHolder.getSettings().setAllowFileAccess(true);
        templateHolder.getSettings().setAllowContentAccess(true);
        templateHolder.getSettings().setAllowUniversalAccessFromFileURLs(true);
        templateHolder.getSettings().setAllowFileAccessFromFileURLs(true);

        //this example expects certain files in external storage
        Storage external = SimpleStorage.getExternalStorage();
        Storage internal = SimpleStorage.getInternalStorage(this);
        internal.copy(external.getFile("Quests/html/p.jpg"), "Quests", "p.jpg");

        //this examples shows that the image can be loaded from external and from internal storage
        templateHolder.loadDataWithBaseURL(
                //"file://"+external.getFile("Quests/html").getAbsolutePath()+"/",
                "file://"+internal.getFile("Quests").getAbsolutePath()+"/",
                external.readTextFile("Quests/html", "test.html"), "text/html", "UTF-8", null
        );
    }

    @Override
    protected void inject(ActivityInjectionComponent activityInjectionComponent) {
        activityInjectionComponent.injectActivity(this);
    }

}
