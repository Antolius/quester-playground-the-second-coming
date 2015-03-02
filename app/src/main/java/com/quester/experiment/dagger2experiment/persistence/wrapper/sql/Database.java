package com.quester.experiment.dagger2experiment.persistence.wrapper.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private SQLiteDatabase database;

    public Database(SQLiteDatabase database) {
        this.database = database;
    }

    public List<Row> query(String tableName, String[] tableValues){

       Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);

       return getRows(tableValues, cursor);
    }

    public List<Row> query(String tableName, String[] tableValues, String whereClause, String[] whereValues){

        Cursor cursor = database.query(tableName, tableValues, whereClause, whereValues, null, null, null);

        return getRows(tableValues, cursor);
    }

    private List<Row> getRows(String[] tableValues, Cursor cursor) {
        List<Row> rows = new ArrayList<>();

        if (!cursor.moveToFirst()) {
            return rows;
        }
        do{
            rows.add(new Row(cursor, tableValues));
        }while(cursor.moveToNext());

        return rows;
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
