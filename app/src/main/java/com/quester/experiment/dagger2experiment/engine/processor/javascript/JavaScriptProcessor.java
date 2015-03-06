package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import com.quester.experiment.dagger2experiment.archive.QuestStorage;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.engine.processor.Processor;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateProvider;
import com.quester.experiment.dagger2experiment.util.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import javax.inject.Inject;

import static com.quester.experiment.dagger2experiment.engine.processor.javascript.ScriptableConverter.getScriptableFromString;
import static com.quester.experiment.dagger2experiment.engine.processor.javascript.ScriptableConverter.getStringFromScriptable;

public class JavaScriptProcessor implements Processor {

    private static final Logger logger = Logger.instance(JavaScriptProcessor.class);

    public static final String PERSISTENT_GAME_OBJECT = "PERSISTENT_GAME_OBJECT";

    private final Scriptable sharedScope;
    private final GameStateProvider gameStateProvider;
    private final QuestStorage storage;

    @Inject
    public JavaScriptProcessor(GameStateProvider gameStateProvider, Scriptable sharedScope, QuestStorage storage) {
        this.gameStateProvider = gameStateProvider;
        this.sharedScope = sharedScope;
        this.storage = storage;
    }

    @Override
    public boolean isCheckpointVisitable(Checkpoint reachedCheckpoint) {

        logger.debug("isCheckpointVisitable called with %s", reachedCheckpoint.toString());

        return !hasScript(reachedCheckpoint) || processCheckpoint(reachedCheckpoint);
    }

    public boolean hasScript(Checkpoint checkpoint) {
        return storage.hasScript(gameStateProvider.getGameState().getQuest(), checkpoint);
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
        boolean result = executeScript(getScript(checkpoint), context, executionScope);
        extractAndSavePersistentGameObject(executionScope);
        return result;
    }

    private String getScript(Checkpoint checkpoint) {
        return storage.getScriptContent(gameStateProvider.getGameState().getQuest(), checkpoint);
    }

    private Scriptable getExecutionScope(Context context) {
        Scriptable executionScope = context.newObject(sharedScope);
        executionScope.setPrototype(sharedScope);
        executionScope.setParentScope(null);

        Scriptable pgo = getScriptableFromString(gameStateProvider.getGameState().getPersistentGameObjectAsString(), sharedScope);
        executionScope.put(PERSISTENT_GAME_OBJECT, executionScope, pgo);
        return executionScope;
    }

    private boolean executeScript(String script, Context context, Scriptable executionScope) {
        Object result = context.evaluateString(executionScope, script, "checkpointScript", 1, null);
        String response = Context.toString(result);
        logger.verbose("executed script and returned %s", response);
        return Boolean.parseBoolean(response);
    }

    private void extractAndSavePersistentGameObject(Scriptable executionScope) {
        Scriptable pgoScriptable = (Scriptable) executionScope.get(PERSISTENT_GAME_OBJECT, executionScope);
        String pgoString = getStringFromScriptable(pgoScriptable, sharedScope);
        logger.verbose("persistentGameObject after script execution: %s", pgoString);
        gameStateProvider.getGameState().setPersistentGameObject(pgoString);
    }

}
