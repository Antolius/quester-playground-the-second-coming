package com.quester.experiment.dagger2experiment.persistence.wrapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {

    private SQLiteDatabase database;

    public Database(SQLiteDatabase database) {
        this.database = database;
    }

    public Rows query(String tableName, String[] tableValues, String whereClause, String[] whereValues){

        Cursor cursor = database.query(tableName, tableValues, whereClause, whereValues, null, null, null);
        return new Rows(cursor);
    }

    public long update(String tableName, ContentValues values, String whereClause, String[] whereValues){

        return database.update(tableName, values, whereClause, whereValues);
    }

    public long insert(String tableName, ContentValues values){

        return database.insert(tableName, null, values);
    }

    public void delete(String tableName, String whereClause, String[] whereValues){

        database.delete(tableName, whereClause, whereValues);
    }
}
