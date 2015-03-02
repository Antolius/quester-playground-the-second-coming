package com.quester.experiment.dagger2experiment.persistence.quest;

import android.content.ContentValues;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.persistence.DatabaseRepository;
import com.quester.experiment.dagger2experiment.persistence.wrapper.sql.Database;
import com.quester.experiment.dagger2experiment.persistence.wrapper.sql.Row;

import java.util.List;

public class CheckpointRepository implements DatabaseRepository<Checkpoint> {

    private Database database;

    public CheckpointRepository(Database database) {

        this.database = database;
    }

    @Override
    public Checkpoint save(Checkpoint element) {

        ContentValues values = new ContentValues();

        values.put("name", element.getName());
        values.put("root", element.isRoot());
        values.put("viewHtmlFileName", element.getViewHtmlFileName());
        values.put("eventsScriptFileName", element.getEventsScriptFileName());

        element.setId(insertOrUpdate(element.getId(), values, "checkpoints"));

        return element;
    }

    @Override
    public Checkpoint findOne(long id) {

        String[] columns = {"id"
                , "name"
                , "root"
                , "viewHtmlFileName"
                , "eventsScriptFileName"
        };
        String[] selectionValues = {String.valueOf(id)};

        List<Row> rows = database.query("checkpoints", columns, "id=?", selectionValues);

        if (rows.isEmpty()) {
            return null;
        }

        Row row = rows.get(0);
        Checkpoint element = new Checkpoint();

        element.setName(row.getString("name"));
        element.setRoot(row.getBoolean("root"));
        element.setViewHtmlFileName(row.getString("viewHtmlFileName"));
        element.setEventsScriptFileName(row.getString("eventsScriptFileName"));

        return element;
    }

    @Override
    public void delete(long id) {

        String[] selectionValues = {String.valueOf(id)};
        database.delete("checkpoints", "id=?", selectionValues);
    }

    @Override
    public boolean exists(long id) {

        String[] columns = {"id"};
        String[] selectionValues = {String.valueOf(id)};

        List<Row> result = database.query("checkpoints", columns, "id=?", selectionValues);

        return !result.isEmpty();
    }

    private long insertOrUpdate(long id, ContentValues values, String tableName) {

        if (exists(id)) {
            return database.update(tableName, values, "id=?", new String[]{String.valueOf(id)});
        }
        return database.insert(tableName, values);
    }
}