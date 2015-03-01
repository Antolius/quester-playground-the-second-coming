package com.quester.experiment.dagger2experiment.persistence;

import android.content.ContentValues;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.data.quest.Quest;
import com.quester.experiment.dagger2experiment.data.quest.QuestGraph;
import com.quester.experiment.dagger2experiment.data.quest.QuestMetaData;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Database;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class QuestRepository implements DatabaseRepository<Quest> {

    private Database database;
    private CheckpointRepository checkpointRepository;
    private static final String[] QUEST_ROWS = {"id", "name", "original_id", "version"};

    public QuestRepository(Database database) {

        this.database = database;
        checkpointRepository = new CheckpointRepository(database);
    }

    @Override
    public Quest save(Quest element) {

        ContentValues values = new ContentValues();

        values.put("name", element.getName());
        if (element.getQuestMetaData() != null) {
            values.put("original_id", element.getQuestMetaData().getOriginalId());
            values.put("version", element.getQuestMetaData().getVersion());
        }

        long modelId = insertOrUpdate(element.getId(), values, "quests");
        element.setId(modelId);

        saveGraph(element);

        return element;
    }

    private long insertOrUpdate(long id, ContentValues values, String tableName) {

        if (exists(id)) {
            return database.update(tableName, values, "id=?", new String[]{String.valueOf(id)});
        }
        return database.insert(tableName, values);
    }

    private void saveGraph(Quest quest) {

        for (Map.Entry<Checkpoint, HashSet<Checkpoint>> entry :
                quest.getQuestGraph().getQuestGraphMap().entrySet()) {

            long keyItemId = checkpointRepository.save(entry.getKey()).getId();
            entry.getKey().setId(keyItemId);

            for (Checkpoint entryValue : entry.getValue()) {

                long valueItemId = checkpointRepository.save(entryValue).getId();
                entryValue.setId(valueItemId);

                ContentValues values = new ContentValues();
                values.put("quest_id", quest.getId());
                values.put("parent_id", keyItemId);
                values.put("child_id", valueItemId);
                database.insert("graph", values);
            }
        }
    }

    @Override
    public Quest findOne(long id) {

        List<Row> results = database.query("quests", QUEST_ROWS, "id=?", new String[]{String.valueOf(id)});

        if (results.isEmpty()) {
            return null;
        }

        return parseQuestFromRow(results.get(0));
    }

    public List<Quest> findAll() {

        List<Row> rows = database.query("quests", QUEST_ROWS);

        List<Quest> quests = new ArrayList<>();

        for(Row row : rows){
            quests.add(parseQuestFromRow(row));
        }

        return quests;
    }

    public Quest findOneByGlobalId(long originalId) {

        List<Row> results = database.query("quests", QUEST_ROWS, "original_id=?", new String[]{String.valueOf(originalId)});

        if (results.isEmpty()) {
            return null;
        }

        return parseQuestFromRow(results.get(0));
    }

    private Quest parseQuestFromRow(Row row) {

        long id = row.getLong("id");

        QuestMetaData metaData = new QuestMetaData();
        metaData.setOriginalId(row.getLong("original_id"));
        metaData.setVersion(row.getLong("version"));

        return new Quest(
                id,
                row.getString("name"),
                findGraph(id),
                metaData
        );
    }

    private QuestGraph findGraph(long id) {

        HashMap<Checkpoint, HashSet<Checkpoint>> map = new HashMap<>();

        List<Row> rows = database.query("graph",
                new String[]{"parent_id", "child_id"},
                "quest_id=?", new String[]{String.valueOf(id)});

        if (rows.isEmpty()) {
            return new QuestGraph(map);
        }

        Map<Long, Checkpoint> helper = new HashMap<>();

        for (Row row : rows) {
            Long parentId = row.getLong("parent_id");
            Checkpoint parentCheckpoint;
            if (!helper.containsKey(parentId)) {
                parentCheckpoint = checkpointRepository.findOne(parentId);
                helper.put(parentId, parentCheckpoint);
                map.put(parentCheckpoint, new HashSet<Checkpoint>());
            } else {
                parentCheckpoint = helper.get(parentId);
            }
            map.get(parentCheckpoint).add(checkpointRepository.findOne(row.getLong("child_id")));
        }

        return new QuestGraph(map);
    }

    @Override
    public void delete(long id) {

        String[] selectionValues = {String.valueOf(id)};

        database.delete("graph", "quest_id=?", selectionValues);

        for (Checkpoint checkpoint : findOne(id).getQuestGraph().getAllCheckpoints()) {
            checkpointRepository.delete(checkpoint.getId());
        }
        database.delete("quests", "id=?", selectionValues);
    }

    @Override
    public boolean exists(long id) {

        return findOne(id) != null;
    }

}
