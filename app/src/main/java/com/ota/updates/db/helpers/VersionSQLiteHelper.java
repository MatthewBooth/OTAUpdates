package com.ota.updates.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ota.updates.db.Contracts;
import com.ota.updates.utils.Constants;

public class VersionSQLiteHelper extends SQLiteOpenHelper implements Constants {

    public VersionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create version table
        String CREATE_VERSION_TABLE = "CREATE TABLE IF NOT EXISTS " + Contracts.VersionContract.TABLE_NAME + " (" +
                Contracts.COLUMN_NAME_ID + " int(11) PRIMARY KEY NOT NULL," +
                Contracts.COLUMN_NAME_FULL_NAME + " text NOT NULL," +
                Contracts.COLUMN_NAME_SLUG + " text NOT NULL," +
                Contracts.COLUMN_NAME_ANDROID_VERSION + " text NOT NULL," +
                Contracts.COLUMN_NAME_CHANGELOG + " text NOT NULL," +
                Contracts.COLUMN_NAME_UPDATED_AT + " updated_at text NOT NULL," +
                Contracts.COLUMN_NAME_CREATED_AT + " text NOT NULL," +
                Contracts.COLUMN_NAME_PUBLISHED_AT + " text NOT NULL," +
                Contracts.COLUMN_NAME_DOWNLOADS + " int(11) NOT NULL," +
                Contracts.COLUMN_NAME_VERSION_NUMBER + " int(11) NOT NULL," +
                Contracts.COLUMN_NAME_FULL_ID + " int(11)," +
                Contracts.COLUMN_NAME_DELTA_ID + " int(11)" +
                ")";

        // create version table
        db.execSQL(CREATE_VERSION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.VersionContract.TABLE_NAME);
        this.onCreate(db);
    }
}
