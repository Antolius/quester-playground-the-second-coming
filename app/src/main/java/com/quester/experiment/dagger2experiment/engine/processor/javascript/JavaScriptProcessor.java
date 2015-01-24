package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.engine.state.GameState;
import com.quester.experiment.dagger2experiment.engine.state.GameStateProvider;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import javax.inject.Inject;

import static com.quester.experiment.dagger2experiment.engine.processor.javascript.ScriptableConverter.getScriptableFromString;
import static com.quester.experiment.dagger2experiment.engine.processor.javascript.ScriptableConverter.getStringFromScriptable;

/**
 * Created by Josip on 14/01/2015!
 */
public class JavaScriptProcessor implements Processor {

    public static final String TAG = "JavaScriptProcessor";
    public static final String PERSISTENT_GAME_OBJECT = "PERSISTENT_GAME_OBJECT";

    private final Scriptable sharedScope;
    private final GameStateProvider gameStateProvider;
    private final JavaScriptLoader javaScriptLoader;

    @Inject
    public JavaScriptProcessor(GameStateProvider gameStateProvider, Scriptable sharedScope, JavaScriptLoader javaScriptLoader) {
        this.gameStateProvider = gameStateProvider;
        this.sharedScope = sharedScope;
        this.javaScriptLoader = javaScriptLoader;
    }

    @Override
    public boolean isCheckpointVisitable(Checkpoint reachedCheckpoint) {
        Logger.d(TAG, "isCheckpointVisitable called with %s", reachedCheckpoint.toString());

        if (javaScriptLoader.isJavaScriptFile(reachedCheckpoint.getEventsScriptFileName())) {
            return processCheckpoint(reachedCheckpoint);
        }

        return true;
    }

    private boolean processCheckpoint(Checkpoint checkpoint) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);

            GameState gameState = gameStateProvider.getGameState();
            Scriptable executionScope = getExecutionScope(context, gameState);

            String response = Context.toString(context.evaluateString(executionScope, javaScriptLoader.loadFile(checkpoint.getEventsScriptFileName()), "checkpointScript", 1, null));
            Logger.v(TAG, "executed script and returned %s", response);

            extractAndSavePersistantGameObject(gameState, executionScope);

            return Boolean.parseBoolean(response);
        } finally {
            Context.exit();
        }
    }

    private Scriptable getExecutionScope(Context context, GameState gameState) {
        Scriptable executionScope = context.newObject(sharedScope);

        executionScope.setPrototype(sharedScope);
        executionScope.setParentScope(null);
        executionScope.put(PERSISTENT_GAME_OBJECT, executionScope, getScriptableFromString(gameState.getPersistentGameObjectAsString(), sharedScope));

        return executionScope;
    }

    private void extractAndSavePersistantGameObject(GameState gameState, Scriptable executionScope) {
        Scriptable pgoScriptable = (Scriptable) executionScope.get(PERSISTENT_GAME_OBJECT, executionScope);
        String pgoString = getStringFromScriptable(pgoScriptable, sharedScope);

        Logger.v(TAG, "persistentGameObject after script execution: %s", pgoString);

        gameState.setPersistentGameObject(pgoString);
    }

}
