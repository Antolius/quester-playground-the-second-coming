package com.quester.experiment.dagger2experiment.persistence;

import android.content.ContentValues;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Database;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Rows;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class QuestRepository implements DatabaseRepository<Quest> {

    private Database database;
    private CheckpointRepository checkpointRepository;

    public QuestRepository(Database database) {

        this.database = database;
        checkpointRepository = new CheckpointRepository(database);
    }

    @Override
    public Quest save(Quest element) {

        ContentValues values = new ContentValues();

        values.put("name", element.getName());

        long modelId = insertOrUpdate(element.getId(), values, "quests");
        element.setId(modelId);

        saveGraph(element);

        return element;
    }

    public void saveGraph(Quest quest) {

        for (Map.Entry<Checkpoint, HashSet<Checkpoint>> entry :
                quest.getQuestGraph().getQuestGraphMap().entrySet()) {

            long keyItemId = checkpointRepository.save(entry.getKey()).getId();
            entry.getKey().setId(keyItemId);

            for (Checkpoint entryValue : entry.getValue()) {

                long valueItemId = checkpointRepository.save(entryValue).getId();
                entryValue.setId(valueItemId);

                ContentValues values = new ContentValues();
                values.put("guest_id", quest.getId());
                values.put("parent_id", keyItemId);
                values.put("child_id", valueItemId);
                database.insert("graph", values);
            }
        }
    }

    @Override
    public Quest find(long id) {

        String[] columns = {"name"};

        Rows result = database.query("quests", columns, "id=?", new String[]{String.valueOf(id)});

        if (!result.moveToFirst()) {
            return null;
        }

        return new Quest(
                id,
                result.getString("name"),
                findGraph(id),
                new QuestMetaData()
        );
    }

    public QuestGraph findGraph(long id) {

        HashMap<Checkpoint, HashSet<Checkpoint>> map = new HashMap<>();

        Rows rows = database.query("graph",
                new String[]{"parent_id", "child_id"},
                "quest_id=?", new String[]{String.valueOf(id)});

        Map<Long, Checkpoint> helper = new HashMap<>();

        if (rows.moveToFirst()) {
            do {
                Long parentId = rows.getLong("parent_id");
                Checkpoint parentCheckpoint;
                if(!helper.containsKey(parentId)){
                    parentCheckpoint = checkpointRepository.find(parentId);
                    helper.put(parentId, parentCheckpoint);
                    map.put(parentCheckpoint, new HashSet<Checkpoint>());
                } else{
                    parentCheckpoint = helper.get(parentId);
                }
                map.get(parentCheckpoint).add(checkpointRepository.find(rows.getLong("child_id")));
            }while (rows.moveToNext());
        }

        return new QuestGraph(map);
    }

    @Override
    public void delete(long id) {

        String[] selectionValues = {String.valueOf(id)};
        //delete checkpoint connections for quest
        database.delete("graph", "quest_id=?", selectionValues);
        //delete checkpoint
        for(Checkpoint checkpoint : find(id).getQuestGraph().getAllCheckpoints()){
            checkpointRepository.delete(checkpoint.getId());
        }
        //delete quest
        database.delete("quests", "id=?", selectionValues);
    }

    @Override
    public boolean exists(long id) {

        String[] columns = {"id"};
        String[] selectionValues = {String.valueOf(id)};

        Rows rows = database.query("quests", columns, "id=?", selectionValues);

        return rows.moveToFirst();
    }

    private long insertOrUpdate(long id, ContentValues values, String tableName) {

        if (exists(id)) {
            return database.update(tableName, values, "id=?", new String[]{String.valueOf(id)});
        }
        return database.insert(tableName, values);
    }

}
