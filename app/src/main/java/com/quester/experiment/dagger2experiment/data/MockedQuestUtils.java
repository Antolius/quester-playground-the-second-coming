package com.quester.experiment.dagger2experiment.data;

import com.quester.experiment.dagger2experiment.data.area.Circle;
import com.quester.experiment.dagger2experiment.data.area.CircularArea;
import com.quester.experiment.dagger2experiment.data.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;

import java.util.ArrayList;

/**
 * Created by Josip on 15/01/2015!
 */
public class MockedQuestUtils {

    public static Quest mockLinearQuest(long id, String name, int size) {
        return new Quest(id, name, mockLinearQuestGraph(size), new QuestMetaData());
    }

    public static Quest mockDiamondQuest(long id, String name) {
        return new Quest(id, name, mockDiamondQuestGraph(), new QuestMetaData());
    }

    public static Quest mockPyramidQuest(long id, String name, int nuberOfRoots) {
        return new Quest(id, name, mockPyramidQuestGraph(nuberOfRoots), new QuestMetaData());
    }

    public static QuestGraph mockPyramidQuestGraph(int numberOfRoots) {
        ArrayList<Checkpoint> checkpoints = mockCheckpoints(numberOfRoots + 1, numberOfRoots);
        QuestGraph questGraph = new QuestGraph(checkpoints);
        for (int i = 0; i < numberOfRoots; i++) {
            questGraph.addEdge(checkpoints.get(i), checkpoints.get(numberOfRoots));
        }
        return questGraph;
    }

    public static QuestGraph mockDiamondQuestGraph() {
        ArrayList<Checkpoint> checkpoints = mockCheckpoints(4, 1);
        QuestGraph questGraph = new QuestGraph(checkpoints);
        questGraph.addEdge(checkpoints.get(0), checkpoints.get(1));
        questGraph.addEdge(checkpoints.get(0), checkpoints.get(2));
        questGraph.addEdge(checkpoints.get(1), checkpoints.get(3));
        questGraph.addEdge(checkpoints.get(2), checkpoints.get(3));
        return questGraph;
    }

    public static QuestGraph mockLinearQuestGraph(int size) {
        ArrayList<Checkpoint> checkpoints = mockCheckpoints(size, 1);
        QuestGraph questGraph = new QuestGraph(checkpoints);
        for (int i = 1; i < size; i++) {
            questGraph.addEdge(checkpoints.get(i - 1), checkpoints.get(i));
        }

        return questGraph;
    }

    public static ArrayList<Checkpoint> mockCheckpoints(int size, int numberOfRoots) {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            checkpoints.add(mockCheckpoint(i, "Checkpoint #" + i, i < numberOfRoots));
        }

        return checkpoints;
    }

    public static Checkpoint mockCheckpoint(long id, String name, boolean isRoot) {
        return mockCheckpoint(id, name, isRoot, mockArea(id), "eventScript-" + id, "viewHtml-" + id);
    }

    public static Checkpoint mockCheckpoint(long id, String name, boolean isRoot, CircularArea area, String eventScriptName, String viewHtmlName) {
        Checkpoint checkpoint = new Checkpoint();

        checkpoint.setId(id);
        checkpoint.setName(name);
        checkpoint.setRoot(isRoot);
        checkpoint.setArea(area);
        checkpoint.setEventsScriptFileName(eventScriptName);
        checkpoint.setViewHtmlFileName(viewHtmlName);

        return checkpoint;
    }

    public static CircularArea mockArea(long id) {
        return mockArea(id, 45.8167, 15.9833, 1000000.0);
    }

    public static CircularArea mockArea(long id, double latitude, double longitude, double radius) {
        Circle circle = new Circle(new Point(latitude, longitude), radius);
        return new CircularArea(circle, id);
    }

}
