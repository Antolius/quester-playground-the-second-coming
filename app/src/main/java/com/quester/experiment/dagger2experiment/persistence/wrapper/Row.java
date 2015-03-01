package com.quester.experiment.dagger2experiment.persistence.wrapper;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

public class Row {

    private Map<String, String> map = new HashMap<>();

    public Row(Cursor cursor, String[] columns) {
        for(String column : columns){
            map.put(column, cursor.getString(cursor.getColumnIndex(column)));
        }
    }

    public long getLong(String columnName){

        return Long.valueOf(map.get(columnName));
    }

    public String getString(String columnName){

        return map.get(columnName);
    }

    public boolean getBoolean(String columnName){

        return "1".equals(getString(columnName));

    }

    public int getInt(String columnName){

        return Integer.valueOf(columnName);
    }

    public short getShort(String columnName){

        return Short.valueOf(columnName);
    }

    public double getDouble(String columnName){

        return Double.valueOf(columnName);
    }

    public float getFloat(String columnName){

        return Float.valueOf(columnName);
    }

}
