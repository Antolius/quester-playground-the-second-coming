package com.quester.experiment.dagger2experiment.repository;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;

import java.util.HashMap;
import java.util.Map;

public class QuestRepository extends Repository{

    public QuestRepository(SQLiteDatabase database) {
        super(database);
    }

    public Quest persistQuest(Quest quest) {

        Quest result = new Quest();

        //persist quest meta data
        result.setName(quest.getName());
        result.setId(database.insert("quests", null, questValues(quest)));

        Map<Checkpoint, Checkpoint> persistedCheckpointsMap = new HashMap<Checkpoint, Checkpoint>();

        //persist all checkpoints
        for (Checkpoint checkpoint : quest.getQuestGraph().getAllCheckpoints()) {
            Checkpoint newCheckpoint = new Checkpoint(checkpoint);
            newCheckpoint.setId(database.insert("checkpoints", null, checkpointValues(result.getId(), checkpoint)));
            persistedCheckpointsMap.put(checkpoint, newCheckpoint);
        }
        result.setQuestGraph(new QuestGraph(persistedCheckpointsMap.values()));

        //persist all connections
        for (Checkpoint parent : quest.getQuestGraph().getAllCheckpoints()) {

            for (Checkpoint child : quest.getQuestGraph().getChildren(parent)) {

                Checkpoint persistedParent = persistedCheckpointsMap.get(parent);
                Checkpoint persistedChild = persistedCheckpointsMap.get(child);

                result.getQuestGraph().addEdge(persistedParent, persistedChild);
                database.insert("connections", null,
                        connectionValues(persistedParent, persistedChild));
            }
        }

        return result;
    }

    public Quest queryQuest(long questId) {

        Map<Long, Checkpoint> checkpointMap = new HashMap<>();

        Cursor questCursor = getQuestCursor(questId);
        if (!questCursor.moveToFirst()) {
            return null;
        }

        String questName = questCursor.getString(questCursor.getColumnIndex("name"));

        Cursor checkpointCursor = getCheckpointCursor(questId);

        checkpointCursor.moveToFirst();
        do {
            long checkpointId = checkpointCursor.getLong(checkpointCursor.getColumnIndex("id"));
            String name = checkpointCursor.getString(checkpointCursor.getColumnIndex("name"));

            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setId(checkpointId);
            checkpoint.setName(name);

            checkpointMap.put(checkpointId, checkpoint);

        } while (checkpointCursor.moveToNext());

        QuestGraph graph = new QuestGraph(checkpointMap.values());

        for (Checkpoint checkpoint : checkpointMap.values()) {

            Cursor connectionCursor = getConnectionCursor(questId, checkpoint.getId());

            if (connectionCursor.moveToFirst()) {
                int index = connectionCursor.getColumnIndex("child_id");
                do {
                    long childId = connectionCursor.getLong(index);
                    graph.addEdge(checkpoint, checkpointMap.get(childId));

                } while (connectionCursor.moveToNext());

            }

        }

        return new Quest(questId, questName, graph, null);
    }

    private Cursor getQuestCursor(long questId) {

        String[] columns = {"id", "name"};
        String[] selectionValues = {String.valueOf(questId)};

        return database.query("quests", columns, "id=?", selectionValues, null, null, null);
    }

    private Cursor getCheckpointCursor(long questId) {

        String[] columns = {"id", "name"};
        String[] selectionValues = {String.valueOf(questId)};

        return database.query("checkpoints", columns, "quest_id=?", selectionValues, null, null, null);
    }

    private Cursor getConnectionCursor(long questId, long parentId) {

        String[] columns = {"child_id"};
        String[] selectionValues = {String.valueOf(parentId)};

        return database.query("connections", columns, "parent_id=?", selectionValues, null, null, null);
    }

    private ContentValues questValues(Quest quest) {
        ContentValues result = new ContentValues();
        result.put("name", quest.getName());
        return result;
    }

    private ContentValues checkpointValues(long questId, Checkpoint checkpoint) {
        ContentValues result = new ContentValues();
        result.put("quest_id", questId);
        result.put("name", checkpoint.getName());
        return result;
    }

    private ContentValues connectionValues(Checkpoint parent, Checkpoint child) {
        ContentValues result = new ContentValues();
        result.put("parent_id", parent.getId());
        result.put("child_id", child.getId());
        return result;
    }
}
