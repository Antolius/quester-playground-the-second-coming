package com.quester.experiment.dagger2experiment.archive;

import android.os.Environment;

import com.sromku.simple.storage.SimpleStorage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class QuestStorageTest {

    @Test
    public void test() throws IOException {

        QuestStorage storage = new QuestStorage(Robolectric.application);

        SimpleStorage.getExternalStorage().createFile("", "123_test.qst", "");
        List<QuestPackage> packageList = storage.findQuestPackages();

        assertEquals(1, packageList.size());
        assertEquals(Long.valueOf(123), packageList.get(0).getId());
        assertEquals("test", packageList.get(0).getName());

        SimpleStorage.getExternalStorage().deleteFile("", "123_test.qst");
    }

    @Test
    public void test2() throws IOException {

        QuestStorage storage = new QuestStorage(Robolectric.application);

        SimpleStorage.getExternalStorage().createFile("", "123_test.qst",
                read(Robolectric.getShadowApplication().getAssets().open("123_test.qst")));
        List<QuestPackage> packageList = storage.findQuestPackages();

        String json = storage.storePackageAndRetrieveScroll(packageList.get(0));

        SimpleStorage.getExternalStorage().deleteFile("", "123_test.qst");

        assertEquals("{\"name\":\"test\"," +
                "\"questGraph\":{" +
                "\"checkpoints\":[" +
                "{\"id\":0,\"name\":\"Checkpoint #0\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}," +
                "{\"id\":1,\"name\":\"Checkpoint #1\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}]," +
                "\"links\":{\"0\":[1],\"1\":[]}}," +
                "\"id\":1," +
                "\"questMetaData\":{\"originalId\":0,\"version\":0}}", json);
    }

    @Test
    public void test3() throws IOException {

        QuestStorage storage = new QuestStorage(Robolectric.application);

        //TODO fix get from assets folder
        SimpleStorage.getExternalStorage().createFile("", "123_test.qst",
                read(Robolectric.getShadowApplication().getAssets().open("123_test.qst")));
        List<QuestPackage> packageList = storage.findQuestPackages();

        String json = storage.retrieveScroll(packageList.get(0));

        SimpleStorage.getExternalStorage().deleteFile("", "123_test.qst");

        assertEquals("{\"name\":\"test\"," +
                "\"questGraph\":{" +
                "\"checkpoints\":[" +
                "{\"id\":0,\"name\":\"Checkpoint #0\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}," +
                "{\"id\":1,\"name\":\"Checkpoint #1\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}]," +
                "\"links\":{\"0\":[1],\"1\":[]}}," +
                "\"id\":1," +
                "\"questMetaData\":{\"originalId\":0,\"version\":0}}", json);
    }

    public byte[] read(InputStream stream) throws IOException {
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
