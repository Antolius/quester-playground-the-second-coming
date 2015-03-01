package com.quester.experiment.dagger2experiment.persistence;

import com.quester.experiment.dagger2experiment.TestUtils;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CheckpointRepositoryTest {

    private CheckpointRepository repository;

    private Checkpoint persistedCheckpoint;
    private Checkpoint retrievedCheckpoint;
    private boolean exists;

    @Before
    public void setUp() {
        repository = new CheckpointRepository(TestUtils.getDatabase());
    }


    @Test
    public void checkpointIsSavedSuccessfully(){

        whenSaveCheckpoint(new Checkpoint(0, "test", true, null, "html", "js"));

        thenCheckpointIsPersisted();
    }

    @Test
    public void checkpointIsRetrievedSuccessfully(){

        givenPersistedCheckpoint(new Checkpoint(0, "test", true, null, "html", "js"));

        whenFindPersistedCheckpoint();

        thenPersistedCheckpointEquals(new Checkpoint(0, "test", true, null, "html", "js"));
    }

    @Test
    public void checkpointExistsIfSaved(){

        givenPersistedCheckpoint(new Checkpoint(0, "test", true, null, "html", "js"));

        whenCheckCheckpointExists();

        thenPersistedCheckpointExists();
    }

    @Test
    public void checkpointIsDeletedSuccessfully(){

        givenPersistedCheckpoint(new Checkpoint(0, "test", true, null, "html", "js"));

        whenDeleteCheckpoint();

        thenPersistedCheckpointNotExists();
    }

    private void givenPersistedCheckpoint(Checkpoint checkpoint){
        persistedCheckpoint = repository.save(checkpoint);
    }

    private void whenSaveCheckpoint(Checkpoint checkpoint) {
        persistedCheckpoint = repository.save(checkpoint);
    }

    private void whenFindPersistedCheckpoint() {
        retrievedCheckpoint = repository.findOne(persistedCheckpoint.getId());
    }

    private void whenCheckCheckpointExists() {
        exists = repository.exists(persistedCheckpoint.getId());
    }

    private void whenDeleteCheckpoint() {
        repository.delete(persistedCheckpoint.getId());
    }

    private void thenPersistedCheckpointEquals(Checkpoint checkpoint) {
        assertEquals(checkpoint.getName(), retrievedCheckpoint.getName());
        assertEquals(checkpoint.getViewHtmlFileName(), retrievedCheckpoint.getViewHtmlFileName());
        assertEquals(checkpoint.getEventsScriptFileName(), retrievedCheckpoint.getEventsScriptFileName());
        assertEquals(checkpoint.isRoot(), retrievedCheckpoint.isRoot());
    }


    private void thenCheckpointIsPersisted() {
        assertNotEquals(0, persistedCheckpoint.getId());
    }

    private void thenPersistedCheckpointExists() {
        assertTrue(exists);
    }

    private void thenPersistedCheckpointNotExists() {
        assertFalse(repository.exists(persistedCheckpoint.getId()));
    }

}

