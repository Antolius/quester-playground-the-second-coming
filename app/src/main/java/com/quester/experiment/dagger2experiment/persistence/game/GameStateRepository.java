package com.quester.experiment.dagger2experiment.persistence.game;

import android.content.Context;

import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.persistence.DatabaseRepository;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.File;

public class GameStateRepository implements DatabaseRepository<GameState> {

    private Storage storage;

    public GameStateRepository(Context context) {
        storage = SimpleStorage.getInternalStorage(context);
        storage.createDirectory("game_state", false);
    }

    @Override
    public GameState save(GameState element) {

        storage.createFile("game_state", "state_" + element.getQuestId() + ".txt",
                element.getPersistentGameObjectAsString());

        return element;
    }

    @Override
    public GameState findOne(long id) {

        String pgo = storage.readTextFile("game_state", "state_" + id + ".txt");
        GameState gameState = new GameState(id, null, pgo);

        return gameState;
    }

    @Override
    public void delete(long id) {

        storage.deleteFile("game_state", "state_" + id + ".txt");
    }

    @Override
    public boolean exists(long id) {
        File file = storage.getFile("gaem_state", "state_" + id + ".txt");
        return file.exists();
    }
}
