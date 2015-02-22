package com.quester.experiment.dagger2experiment.engine.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DummyTest {

    @Test
    public void dummyTest() {

        assertEquals(true, true);
    }

}
