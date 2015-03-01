package com.quester.experiment.dagger2experiment;

import android.database.sqlite.SQLiteDatabase;

import com.quester.experiment.dagger2experiment.persistence.wrapper.Database;
import com.sromku.simple.storage.SimpleStorage;

import org.robolectric.Robolectric;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

    public static Database getDatabase() {
        SQLiteDatabase db = SQLiteDatabase.create(null);
        db.execSQL("CREATE TABLE quests(\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   name TEXT,\n" +
                "   original_id INTEGER,\n" +
                "   version INTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE checkpoints(\n" +
                "   id INTEGER PRIMARY KEY   NOT NULL,\n" +
                "   name TEXT,\n" +
                "   root INTEGER,\n" +
                "   viewHtmlFileName TEXT,\n" +
                "   eventsScriptFileName TEXT\n" +
                ");");
        db.execSQL("CREATE TABLE graph(\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   quest_id INTEGER NOT NULL,\n" +
                "   parent_id INTEGER NOT NULL,\n" +
                "   child_id INTEGER NOT NULL\n" +
                ");");
        return new Database(db);
    }

    public static void moveQuestPackageFromAssetsToExternalStorage(String name) throws IOException {

        //fix location of file should be assets not tmp file
        SimpleStorage.getExternalStorage().createFile("", name,
                read(Robolectric.getShadowApplication().getAssets().open(name)));
    }

    private static byte[] read(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
}
