package com.quester.experiment.dagger2experiment.repository;

import android.database.sqlite.SQLiteDatabase;

public class GameRepository extends Repository{

    public GameRepository(SQLiteDatabase database) {
        super(database);
    }
}
