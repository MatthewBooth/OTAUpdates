package com.ota.updates.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ota.updates.utils.Constants;

public class AddonSQLiteHelper extends SQLiteOpenHelper implements Constants {

    public AddonSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create addon table
        String CREATE_ADDON_TABLE = "CREATE TABLE IF NOT EXISTS " + ADDON_TABLE_NAME + " (" +
                NAME_ID + " int(11) PRIMARY KEY NOT NULL," +
                NAME_NAME + " text NOT NULL," +
                NAME_SLUG + " text NOT NULL," +
                NAME_DESCRIPTION + " text NOT NULL," +
                NAME_UPDATED_AT + " updated_at text NOT NULL," +
                NAME_CREATED_AT + " text NOT NULL," +
                NAME_PUBLISHED_AT + " text NOT NULL," +
                NAME_DOWNLOADS + " int(11) NOT NULL," +
                NAME_SIZE + " int(32) NOT NULL," +
                NAME_MD5 + " text(32) NOT NULL," +
                NAME_DOWNLOAD_LINK + " text NOT NULL" +
                ")";

        // create addon table
        db.execSQL(CREATE_ADDON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ADDON_TABLE_NAME);
        this.onCreate(db);
    }
}
