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

    public static Quest getMockedLinearQuest(long id, String name, int size) {
        return new Quest(id, name, getMockedLinearQuestGraph(size), new QuestMetaData());
    }

    public static Quest getMockedDiamondQuest(long id, String name) {
        return new Quest(id, name, getMockedDiamondQuestGraph(), new QuestMetaData());
    }

    public static Quest getMockedPyramidQuest(long id, String name, int nuberOfRoots) {
        return new Quest(id, name, getMockedPyramidQuestGraph(nuberOfRoots), new QuestMetaData());
    }

    public static QuestGraph getMockedPyramidQuestGraph(int numberOfRoots) {
        ArrayList<Checkpoint> checkpoints = getMockedCheckpoints(numberOfRoots + 1, numberOfRoots);
        QuestGraph questGraph = new QuestGraph(checkpoints);
        for (int i = 0; i < numberOfRoots; i++) {
            questGraph.addEdge(checkpoints.get(i), checkpoints.get(numberOfRoots));
        }
        return questGraph;
    }

    public static QuestGraph getMockedDiamondQuestGraph() {
        ArrayList<Checkpoint> checkpoints = getMockedCheckpoints(4, 1);
        QuestGraph questGraph = new QuestGraph(checkpoints);
        questGraph.addEdge(checkpoints.get(0), checkpoints.get(1));
        questGraph.addEdge(checkpoints.get(0), checkpoints.get(2));
        questGraph.addEdge(checkpoints.get(1), checkpoints.get(3));
        questGraph.addEdge(checkpoints.get(2), checkpoints.get(3));
        return questGraph;
    }

    public static QuestGraph getMockedLinearQuestGraph(int size) {
        ArrayList<Checkpoint> checkpoints = getMockedCheckpoints(size, 1);
        QuestGraph questGraph = new QuestGraph(checkpoints);
        for (int i = 1; i < size; i++) {
            questGraph.addEdge(checkpoints.get(i - 1), checkpoints.get(i));
        }

        return questGraph;
    }

    public static ArrayList<Checkpoint> getMockedCheckpoints(int size, int numberOfRoots) {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            checkpoints.add(getMockedCheckpoint(i, "Checkpoint #" + i, i < numberOfRoots));
        }

        return checkpoints;
    }

    public static Checkpoint getMockedCheckpoint(long id, String name, boolean isRoot) {
        return getMockedCheckpoint(id, name, isRoot, getMockedArea(id), "eventScript-" + id, "viewHtml-" + id);
    }

    public static Checkpoint getMockedCheckpoint(long id, String name, boolean isRoot, CircularArea area, String eventScriptName, String viewHtmlName) {
        Checkpoint checkpoint = new Checkpoint();

        checkpoint.setId(id);
        checkpoint.setName(name);
        checkpoint.setRoot(isRoot);
        checkpoint.setArea(area);
        checkpoint.setEventsScriptFileName(eventScriptName);
        checkpoint.setViewHtmlFileName(viewHtmlName);

        return checkpoint;
    }

    public static CircularArea getMockedArea(long id) {
        return getMockedArea(id, 45.8167, 15.9833, 150.0);
    }

    public static CircularArea getMockedArea(long id, double latitude, double longitude, double radius) {
        Circle circle = new Circle(new Point(latitude, longitude), radius);
        return new CircularArea(circle, id);
    }

}
