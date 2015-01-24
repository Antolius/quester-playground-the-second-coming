package com.quester.experiment.dagger2experiment.engine.processor;

import android.content.Context;

import com.quester.experiment.dagger2experiment.util.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Josip on 24/01/2015!
 */
public class ScriptLoader {

    private static final String TAG = "ScriptLoader";

    public String readFile(String fileName, Context context) {
        FileInputStream inputStream;

        try {
            inputStream = context.openFileInput(fileName);
            String fileContent = convertStreamFoString(inputStream);
            inputStream.close();
            return fileContent;
        } catch (IOException e) {
            Logger.e(TAG, "failed to load file %s, error message: %s", fileName, e.getMessage());
            throw new ScriptLoadingException();
        }
    }

    private String convertStreamFoString(FileInputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public static class ScriptLoadingException extends RuntimeException {
    }
}
