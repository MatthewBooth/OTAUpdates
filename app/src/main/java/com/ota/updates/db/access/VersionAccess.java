package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;

import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.VersionItem;

public class VersionAccess extends BaseAccess<VersionItem> {

    public VersionAccess(Context context) {
        mDbHelper = new VersionSQLiteHelper(context);
    }

    public void put(VersionItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_FULL_NAME, item.getFullName());
        values.put(NAME_SLUG, item.getSlug());
        values.put(NAME_ANDROID_VERSION, item.getAndroidVersion());
        values.put(NAME_CHANGELOG, item.getChangelog());
        values.put(NAME_UPDATED_AT, item.getUpdatedAt());
        values.put(NAME_CREATED_AT, item.getCreatedAt());
        values.put(NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_VERSION_NUMBER, item.getVersionNumber());
        values.put(NAME_FULL_ID, item.getFullUploadId());
        values.put(NAME_DELTA_ID, item.getDeltaUploadId());

        long newRowId;
        newRowId = mDatabase.insert(
                VERSION_TABLE_NAME,
                NAME_FULL_ID,
                values
        );
    }
}
