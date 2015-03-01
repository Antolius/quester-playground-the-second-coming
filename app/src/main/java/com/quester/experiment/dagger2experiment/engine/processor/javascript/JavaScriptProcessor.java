package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
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
        Logger.debug(TAG, "isCheckpointVisitable called with %s", reachedCheckpoint.toString());
        if (javaScriptLoader.isJavaScriptFile(reachedCheckpoint.getEventsScriptFileName())) {
            return processCheckpoint(reachedCheckpoint);
        }
        return true;
    }

    private boolean processCheckpoint(Checkpoint checkpoint) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            return handleScriptExecution(checkpoint, context);
        } finally {
            Context.exit();
        }
    }

    private boolean handleScriptExecution(Checkpoint checkpoint, Context context) {
        Scriptable executionScope = getExecutionScope(context);
        String script = loadScript(checkpoint);
        String response = executeScript(script, context, executionScope);
        extractAndSavePersistentGameObject(executionScope);
        return Boolean.parseBoolean(response);
    }

    private Scriptable getExecutionScope(Context context) {
        Scriptable executionScope = context.newObject(sharedScope);
        executionScope.setPrototype(sharedScope);
        executionScope.setParentScope(null);

        Scriptable pgo = getScriptableFromString(gameStateProvider.getGameState().getPersistentGameObjectAsString(), sharedScope);
        executionScope.put(PERSISTENT_GAME_OBJECT, executionScope, pgo);
        return executionScope;
    }

    private String loadScript(Checkpoint checkpoint) {
        return javaScriptLoader.loadFile(checkpoint.getEventsScriptFileName());
    }

    private String executeScript(String script, Context context, Scriptable executionScope) {
        Object result = context.evaluateString(executionScope, script, "checkpointScript", 1, null);
        String response = Context.toString(result);
        Logger.verbose(TAG, "executed script and returned %s", response);
        return response;
    }

    private void extractAndSavePersistentGameObject(Scriptable executionScope) {
        Scriptable pgoScriptable = (Scriptable) executionScope.get(PERSISTENT_GAME_OBJECT, executionScope);
        String pgoString = getStringFromScriptable(pgoScriptable, sharedScope);
        Logger.verbose(TAG, "persistentGameObject after script execution: %s", pgoString);
        gameStateProvider.getGameState().setPersistentGameObject(pgoString);
    }

}
