package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ota.updates.db.Contracts;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.rom.Version;

public class VersionAccess {
    private SQLiteDatabase mDatabase;
    private VersionSQLiteHelper mDbHelper;

    public VersionAccess(Context context) {
        mDbHelper = new VersionSQLiteHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public void put(Version version) {
        ContentValues values = new ContentValues();
        values.put(Contracts.VersionContract.COLUMN_NAME_ID, version.getId());
        values.put(Contracts.VersionContract.COLUMN_NAME_FULL_NAME, version.getFullName());
        values.put(Contracts.VersionContract.COLUMN_NAME_SLUG, version.getSlug());
        values.put(Contracts.VersionContract.COLUMN_NAME_ANDROID_VERSION, version.getAndroidVersion());
        values.put(Contracts.VersionContract.COLUMN_NAME_CHANGELOG, version.getChangelog());
        values.put(Contracts.VersionContract.COLUMN_NAME_UPDATED_AT, version.getUpdatedAt());
        values.put(Contracts.VersionContract.COLUMN_NAME_CREATED_AT, version.getCreatedAt());
        values.put(Contracts.VersionContract.COLUMN_NAME_PUBLISHED_AT, version.getPublishedAt());
        values.put(Contracts.VersionContract.COLUMN_NAME_DOWNLOADS, version.getDownloads());
        values.put(Contracts.VersionContract.COLUMN_NAME_VERSION_NUMBER, version.getVersionNumber());
        values.put(Contracts.VersionContract.COLUMN_NAME_FULL_ID, version.getFullUpload().getId());
        values.put(Contracts.VersionContract.COLUMN_NAME_DELTA_ID, version.getDeltaUpload().getId());

        long newRowId;
        newRowId = mDatabase.insert(
                Contracts.VersionContract.TABLE_NAME,
                Contracts.VersionContract.COLUMN_NAME_FULL_ID,
                values
        );

    }
}
