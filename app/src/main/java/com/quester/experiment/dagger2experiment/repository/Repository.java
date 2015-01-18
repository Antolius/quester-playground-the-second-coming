package com.quester.experiment.dagger2experiment.repository;

import android.database.sqlite.SQLiteDatabase;

public abstract class Repository {

    protected SQLiteDatabase database;

    public Repository(SQLiteDatabase database) {
        this.database = database;
    }
}
