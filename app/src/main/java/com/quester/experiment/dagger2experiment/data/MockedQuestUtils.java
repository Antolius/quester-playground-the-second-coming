package com.quester.experiment.dagger2experiment.data;

import android.content.Context;

import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.Circle;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.circle.CircularArea;
import com.quester.experiment.dagger2experiment.data.checkpoint.area.Point;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

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
        return mockCheckpoint(id, name, isRoot, mockArea(id), "mockedScript", "mockedView");
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
        return new CircularArea(circle);
    }

    public static void createFilesForCheckpoints(Collection<Checkpoint> checkpoints, Context context) {
        for (Checkpoint c : checkpoints) {
            c.setEventsScriptFileName("eventScript-" + c.getId() + ".js");
            c.setViewHtmlFileName("viewHtml-" + c.getId() + ".html");
            createFilesForCheckpoint(c, context);
        }
    }

    public static void createFilesForCheckpoint(Checkpoint checkpoint, Context context) {
        createFilesForCheckpoint(checkpoint, getDefaultScript(checkpoint), getDefaultView(checkpoint), context);
    }

    private static String getDefaultView(Checkpoint checkpoint) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>Welcome to " + checkpoint.getName() + "</h1>\n" +
                "<p> checkpointId = " + checkpoint.getId() + "</p>\n" +
                "<p id=\"pgo\"></p>\n" +
                "<script>\n" +
                "var pgo = JSON.parse(gameState.getPersistentGameObject());\n" +
                "if (pgo.viewdHtmls == null) {\n" +
                "   pgo.viewdHtmls = [];\n" +
                "}\n" +
                "pgo.viewdHtmls[pgo.viewdHtmls.length] = {" +
                "       'viewName' : '" + checkpoint.getViewHtmlFileName() + "',\n" +
                "       'checkpointName' : '" + checkpoint.getName() + "',\n" +
                "       'checkpointId' : " + checkpoint.getId() + "\n" +
                "   };\n" +
                "document.getElementById(\"pgo\").innerHTML = JSON.stringify(pgo);\n" +
                "gameState.savePersistentGameObject(JSON.stringify(pgo));\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
    }

    private static String getDefaultScript(Checkpoint checkpoint) {
        return "function f() {\n" +
                "   if (PERSISTENT_GAME_OBJECT.executedScripts == null) {\n" +
                "       PERSISTENT_GAME_OBJECT.executedScripts = [];\n" +
                "   } else {\n" +
                "       if (PERSISTENT_GAME_OBJECT.viewdHtmls == null || PERSISTENT_GAME_OBJECT.viewdHtmls.length < PERSISTENT_GAME_OBJECT.executedScripts.length) {\n" +
                "           return false;\n" +
                "       }\n" +
                "   }\n" +
                "   PERSISTENT_GAME_OBJECT.executedScripts[PERSISTENT_GAME_OBJECT.executedScripts.length] = {\n" +
                "       'scriptName' : '" + checkpoint.getEventsScriptFileName() + "',\n" +
                "       'checkpointName' : '" + checkpoint.getName() + "',\n" +
                "       'checkpointId' : " + checkpoint.getId() + "\n" +
                "   };\n" +
                "   return true;\n" +
                "};\n" +
                "f();\n";
    }

    public static void createFilesForCheckpoint(Checkpoint checkpoint, String script, String view, Context context) {
        createFile(checkpoint.getEventsScriptFileName(), script, context);
        createFile(checkpoint.getViewHtmlFileName(), view, context);
    }

    private static void createFile(String fileName, String fileContent, Context context) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
