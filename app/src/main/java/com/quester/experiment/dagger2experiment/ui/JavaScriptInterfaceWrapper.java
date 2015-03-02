package com.quester.experiment.dagger2experiment.ui;

import android.webkit.JavascriptInterface;

import com.quester.experiment.dagger2experiment.data.game.GameState;

/**
 * Created by Josip on 25/01/2015!
 */
public class JavaScriptInterfaceWrapper {

    private final GameState gameState;

    public JavaScriptInterfaceWrapper(GameState gameState) {
        this.gameState = gameState;
    }

    @JavascriptInterface
    public String getPersistentGameObject() {
        return gameState.getPersistentGameObjectAsString();
    }

    @JavascriptInterface
    public void savePersistentGameObject(String persistentGameObjectString) {
        gameState.setPersistentGameObject(persistentGameObjectString);
    }
}
