package com.quester.experiment.dagger2experiment.data.parcel;

import com.quester.experiment.dagger2experiment.data.MockedQuestUtils;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.Circle;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.CircularArea;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle.Rectangle;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.rectangle.RectangularArea;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ParcellingTest {

    @Test
    public void circularAreaIsParcelledCorrectly() {

        CircularArea area = new CircularArea(new Circle(new Point(1.0, 2.0), 3.0));

        CircularArea unwrapped = Parcels.unwrap(Parcels.wrap(area));

        assertEquals(1.0, unwrapped.getCircle().getCenter().getLatitude(), 1e-8);
        assertEquals(2.0, unwrapped.getCircle().getCenter().getLongitude(), 1e-8);
        assertEquals(3.0, unwrapped.getCircle().getRadius(), 1e-8);
    }

    @Test
    public void rectangularAreaIsParcelledCorrectly() {

        RectangularArea area = new RectangularArea(
                new Rectangle(new Point(1.0, 2.0), new Point(3.0, 4.0)));

        RectangularArea unwrapped = Parcels.unwrap(Parcels.wrap(area));

        assertEquals(1.0, unwrapped.getRectangle().getUpperLeftCorner().getLatitude(), 1e-8);
        assertEquals(2.0, unwrapped.getRectangle().getUpperLeftCorner().getLongitude(), 1e-8);
        assertEquals(3.0, unwrapped.getRectangle().getLowerRightCorner().getLatitude(), 1e-8);
        assertEquals(4.0, unwrapped.getRectangle().getLowerRightCorner().getLongitude(), 1e-8);
    }

    @Test
    public void checkpointIsParcelledCorrectly() {

        Checkpoint checkpoint = new Checkpoint();
        checkpoint.setId(1);
        checkpoint.setName("ch1");
        checkpoint.setRoot(true);
        checkpoint.setViewHtmlFileName("html");
        checkpoint.setEventsScriptFileName("js");
        checkpoint.setArea(new CircularArea(new Circle(new Point(1.0, 2.0), 3.0)));

        Checkpoint unwrapped = Parcels.unwrap(Parcels.wrap(checkpoint));

        assertEquals(1, unwrapped.getId());
        assertEquals("ch1", unwrapped.getName());
        assertEquals(true, unwrapped.isRoot());
        assertEquals("html", unwrapped.getViewHtmlFileName());
        assertEquals("js", unwrapped.getEventsScriptFileName());
        assertTrue(unwrapped.getArea() instanceof CircularArea);
    }

    @Test
    public void questIsParcelledCorrectly() {

        Quest quest = MockedQuestUtils.mockLinearQuest(1, "test", 3);
        quest.setQuestMetaData(new QuestMetaData(1L, 2L));

        Quest unwrapped = Parcels.unwrap(Parcels.wrap(quest));

        assertEquals(1, quest.getId());
        assertEquals("test", unwrapped.getName());
        assertEquals(1L, unwrapped.getQuestMetaData().getOriginalId());
        assertEquals(2L, unwrapped.getQuestMetaData().getVersion());
        assertEquals(3, unwrapped.getQuestGraph().getAllCheckpoints().size());

    }
}
