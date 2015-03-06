package com.quester.experiment.dagger2experiment.persistence.game;

import android.content.Context;

import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.persistence.DatabaseRepository;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

public class GameStateRepository implements DatabaseRepository<GameState> {

    private static final String GAME_STATE_DIRECTORY = "game_state";

    private Storage storage;

    public GameStateRepository(Context context) {
        storage = SimpleStorage.getInternalStorage(context);
        storage.createDirectory(GAME_STATE_DIRECTORY, false);
    }

    @Override
    public GameState save(GameState element) {

        storage.createFile(
                GAME_STATE_DIRECTORY,
                getStateFileName(element.getQuestId()),
                element.getPersistentGameObjectAsString());

        return element;
    }

    @Override
    public GameState findOne(long id) {

        return new GameState(
                id,
                null,
                storage.readTextFile("game_state", getStateFileName(id)));
    }

    @Override
    public void delete(long id) {

        storage.deleteFile(GAME_STATE_DIRECTORY, getStateFileName(id));
    }

    @Override
    public boolean exists(long id) {

        return storage.isFileExist(GAME_STATE_DIRECTORY, getStateFileName(id));
    }

    private String getStateFileName(long id){
        return "state_" + id + ".txt";
    }
}
