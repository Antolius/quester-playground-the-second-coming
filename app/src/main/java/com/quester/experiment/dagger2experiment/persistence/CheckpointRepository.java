package com.quester.experiment.dagger2experiment.persistence;

import android.content.ContentValues;

import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Database;
import com.quester.experiment.dagger2experiment.persistence.wrapper.Rows;

public class CheckpointRepository implements DatabaseRepository<Checkpoint>{

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
    public Checkpoint find(long id) {

        String[] columns = {"id"
                ,"name"
                ,"root"
                ,"viewHtmlFileName"
                ,"eventsScriptFileName"
        };
        String[] selectionValues = {String.valueOf(id)};

        Rows cursor = database.query("checkpoints", columns, "id=?", selectionValues);

        if (!cursor.moveToFirst()) {
            return null;
        }
        Checkpoint element = new Checkpoint();

        element.setName(java.lang.String.valueOf(cursor.getString("name")));
        element.setRoot(java.lang.Boolean.valueOf(cursor.getString("root")));
        element.setViewHtmlFileName(java.lang.String.valueOf(cursor.getString("viewHtmlFileName")));
        element.setEventsScriptFileName(java.lang.String.valueOf(cursor.getString("eventsScriptFileName")));

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

        Rows result = database.query("checkpoints", columns, "id=?", selectionValues);

        return result.moveToFirst();
    }

    private long insertOrUpdate(long id, ContentValues values, String tableName){

        if(exists(id)){
            return database.update(tableName, values, "id=?", new String[]{String.valueOf(id)});
        }
        return database.insert(tableName, values);
    }
}