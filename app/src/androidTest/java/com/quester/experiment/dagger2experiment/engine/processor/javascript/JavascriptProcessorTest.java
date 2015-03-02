package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.game.GameState;
import com.quester.experiment.dagger2experiment.engine.provider.GameStateProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class JavascriptProcessorTest {

    @Test
    public void test() {

        Scriptable scriptable = Context.enter().initStandardObjects(null, false);

        GameStateProvider provider = mock(GameStateProvider.class);
        when(provider.getGameState()).thenReturn(new GameState(1, null, "{}"));

        JavaScriptLoader loader = mock(JavaScriptLoader.class);
        when(loader.isJavaScriptFile(anyString())).thenReturn(true);
        when(loader.loadFile(anyString())).thenReturn("function f(){ return true;}; f();");

        JavaScriptProcessor processor = new JavaScriptProcessor(provider, scriptable, loader);

        boolean isVisitable = processor.isCheckpointVisitable(new Checkpoint());

        assertTrue(isVisitable);
    }

}
