package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;

import com.ota.updates.db.Contracts;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.VersionItem;

public class VersionAccess extends BaseAccess<VersionItem> {

    public VersionAccess(Context context) {
        mDbHelper = new VersionSQLiteHelper(context);
    }

    public void put(VersionItem item) {
        ContentValues values = new ContentValues();
        values.put(Contracts.COLUMN_NAME_ID, item.getId());
        values.put(Contracts.COLUMN_NAME_FULL_NAME, item.getFullName());
        values.put(Contracts.COLUMN_NAME_SLUG, item.getSlug());
        values.put(Contracts.COLUMN_NAME_ANDROID_VERSION, item.getAndroidVersion());
        values.put(Contracts.COLUMN_NAME_CHANGELOG, item.getChangelog());
        values.put(Contracts.COLUMN_NAME_UPDATED_AT, item.getUpdatedAt());
        values.put(Contracts.COLUMN_NAME_CREATED_AT, item.getCreatedAt());
        values.put(Contracts.COLUMN_NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(Contracts.COLUMN_NAME_DOWNLOADS, item.getDownloads());
        values.put(Contracts.COLUMN_NAME_VERSION_NUMBER, item.getVersionNumber());
        values.put(Contracts.COLUMN_NAME_FULL_ID, item.getFullUploadId());
        values.put(Contracts.COLUMN_NAME_DELTA_ID, item.getDeltaUploadId());

        long newRowId;
        newRowId = mDatabase.insert(
                Contracts.VersionContract.TABLE_NAME,
                Contracts.COLUMN_NAME_FULL_ID,
                values
        );
    }
}
