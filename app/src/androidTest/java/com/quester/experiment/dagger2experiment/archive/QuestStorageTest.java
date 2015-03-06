package com.quester.experiment.dagger2experiment.archive;

import com.quester.experiment.dagger2experiment.TestUtils;
import com.sromku.simple.storage.SimpleStorage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class QuestStorageTest {

    private QuestStorage storage;

    private List<QuestPackage> packageList;
    private String jsonScroll;
    private QuestPackage questPackage;
    private boolean isStored;

    @Before
    public void setUp() {
        storage = new QuestStorage(Robolectric.application);
    }

    @Test
    public void questPackagesAreRetrievedCorrectly() throws IOException {

        givenPackageFromAssets("123_test.qst");

        whenFindQuestPackages();

        thenFoundPackages(1);
        thenFirstPackageEquals(new QuestPackage(123L, "test", null));
    }

    @Test
    public void questIsUnpackedToInternalStorage() throws IOException {

        givenPackageInInternalStorage("123_test.qst");

        whenStorePackage();

        thenFolderExistsInInternalStorage("123_test");
        thenStoringSucceeded();
    }

    @Test
    public void questFailedToUnpackedToInternalStorage() throws IOException {

        givenPackageWithNoFileInInternalStorage();

        whenStorePackage();

        thenStoringFailed();
    }

    //simple storage fails to delete folder
    @Ignore
    @Test
    public void questIsDeletedFromInternalStorage() throws IOException {

        givenPackageInInternalStorage("123_test.qst");

        whenRemoveQuest(123L, "test");

        thenFolderNotExistsInInternalStorage("123_test");
    }

    @Test
    public void questScrollIsCorrectlyExtracted() throws IOException {

        givenPackageFromAssets("123_test.qst");

        whenExtractQuestScroll();

        thenQuestScrollIs("{\"name\":\"test\"," +
                "\"questGraph\":{" +
                "\"checkpoints\":[" +
                "{\"id\":0,\"name\":\"Checkpoint #0\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}," +
                "{\"id\":1,\"name\":\"Checkpoint #1\",\"html\":\"mockedView\",\"script\":\"mockedScript\"}]," +
                "\"links\":{\"0\":[1],\"1\":[]}}," +
                "\"id\":1," +
                "\"questMetaData\":{\"originalId\":123,\"version\":0}}");
    }

    private void givenPackageFromAssets(String name) throws IOException {
        TestUtils.moveQuestPackageFromAssetsToExternalStorage(name);
    }

    private void givenPackageInInternalStorage(String name) throws IOException {
        TestUtils.moveQuestPackageFromAssetsToExternalStorage(name);
        packageList = storage.findQuestPackages();
        questPackage = packageList.get(0);
        storage.storePackage(packageList.get(0));
    }

    private void givenPackageWithNoFileInInternalStorage() throws IOException {
        questPackage = new QuestPackage(123L, "test", null);
    }

    private void whenFindQuestPackages() {
        packageList = storage.findQuestPackages();
    }

    private void whenStorePackage() {
        isStored = storage.storePackage(questPackage);
    }

    private void whenExtractQuestScroll() {
        packageList = storage.findQuestPackages();
        jsonScroll = storage.extractQuestJson(packageList.get(0));
    }

    private void whenRemoveQuest(long id, String questName) {
        storage.removeQuest(new QuestPackage(id, questName, null));
    }

    private void thenFoundPackages(int numberOfQuestPackages) {

        assertEquals(numberOfQuestPackages, packageList.size());
    }

    private void thenFirstPackageEquals(QuestPackage questPackage) {

        assertEquals(questPackage.getName(), packageList.get(0).getName());
        assertEquals(questPackage.getId(), packageList.get(0).getId());
    }

    private void thenQuestScrollIs(String questJson) {

        assertEquals(jsonScroll, questJson);
    }

    private void thenFolderExistsInInternalStorage(String questFolder) {
        assertTrue(SimpleStorage.getInternalStorage(Robolectric.application).isDirectoryExists("Quests/" + questFolder));
    }

    private void thenFolderNotExistsInInternalStorage(String questFolder) {
        assertFalse(SimpleStorage.getInternalStorage(Robolectric.application).isDirectoryExists("Quests/" + questFolder));
    }

    private void thenStoringFailed() {

        assertEquals(false, isStored);
    }

    private void thenStoringSucceeded() {
        assertEquals(true, isStored);
    }

}
