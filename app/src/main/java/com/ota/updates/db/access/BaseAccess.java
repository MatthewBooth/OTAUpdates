package com.ota.updates.db.access;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ota.updates.utils.Constants;

public abstract class BaseAccess<T> implements Constants {
    protected SQLiteDatabase mDatabase;
    protected SQLiteOpenHelper mDbHelper;

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public abstract void put(T item);
}
