package com.quester.experiment.dagger2experiment.persistence.wrapper;

import android.database.Cursor;

public class Rows {

    private Cursor cursor;

    public Rows(Cursor cursor) {
        this.cursor = cursor;
    }

    public long getLong(String columnName){

        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public String getString(String columnName){

        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public int getInt(String columnName){

        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public short getShort(String columnName){

        return cursor.getShort(cursor.getColumnIndex(columnName));
    }

    public double getDouble(String columnName){

        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    public float getFloat(String columnName){

        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }


    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    public boolean moveToNext(){
        return cursor.moveToNext();
    }

}
