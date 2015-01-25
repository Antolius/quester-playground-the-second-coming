package com.quester.experiment.dagger2experiment.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Josip on 24/01/2015!
 */
public class FileLoader {

    private static final String TAG = "ScriptLoader";

    public static String readFile(String fileName, Context context) {
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

    private static String convertStreamFoString(FileInputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            reader.close();
        }
        return sb.toString();
    }

    public static class ScriptLoadingException extends RuntimeException {
    }
}
