package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import android.content.Context;

import com.quester.experiment.dagger2experiment.engine.EngineScope;
import com.quester.experiment.dagger2experiment.engine.processor.ScriptLoader;

import javax.inject.Inject;

/**
 * Created by Josip on 24/01/2015!
 */
public class JavaScriptLoader extends ScriptLoader {

    public static final String JAVA_SCRIPT_EXTENSION = "js";
    private Context context;

    @Inject
    public JavaScriptLoader(@EngineScope Context context) {
        this.context = context;
    }

    public String loadFile(String fileName) {
        return readFile(fileName, context);
    }

    public boolean isJavaScriptFile(String fileName) {
        String filenameArray[] = fileName.split("\\.");

        String extension = "";
        if (filenameArray.length > 0) {
            extension = filenameArray[filenameArray.length - 1];
        }

        return JAVA_SCRIPT_EXTENSION.equalsIgnoreCase(extension);
    }
}
