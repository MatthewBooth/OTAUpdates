package com.ota.updates.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ota.updates.db.Contracts;
import com.ota.updates.utils.Constants;

public class UploadSQLiteHelper extends SQLiteOpenHelper implements Constants {

    public UploadSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create upload table
        String CREATE_UPLOAD_TABLE = "CREATE TABLE IF NOT EXISTS " + Contracts.UploadContract.TABLE_NAME + " (" +
                Contracts.COLUMN_NAME_ID + " int(11) PRIMARY KEY NOT NULL," +
                Contracts.COLUMN_NAME_SIZE + " int(32) NOT NULL," +
                Contracts.COLUMN_NAME_MD5 + " text(32) NOT NULL," +
                Contracts.COLUMN_NAME_STATUS + " text NOT NULL," +
                Contracts.COLUMN_NAME_DOWNLOADS + " int(11) NOT NULL," +
                Contracts.COLUMN_NAME_DOWNLOAD_LINK + " text NOT NULL" +
                ")";
        // create upload table
        db.execSQL(CREATE_UPLOAD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contracts.UploadContract.TABLE_NAME);
        this.onCreate(db);
    }
}
