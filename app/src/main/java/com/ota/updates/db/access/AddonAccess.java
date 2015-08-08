package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;

import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;

public class AddonAccess extends BaseAccess<AddonItem> {
    public AddonAccess(Context context) {
        mDbHelper = new AddonSQLiteHelper(context);
    }

    @Override
    public void put(AddonItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_NAME, item.getName());
        values.put(NAME_SLUG, item.getSlug());
        values.put(NAME_DESCRIPTION, item.getDescription());
        values.put(NAME_UPDATED_AT, item.getUpdatedAt());
        values.put(NAME_CREATED_AT, item.getCreatedAt());
        values.put(NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(NAME_SIZE, item.getSize());
        values.put(NAME_MD5, item.getMd5());
        values.put(NAME_DOWNLOAD_LINK, item.getDownloadLink());

        long newRowId;
        newRowId = mDatabase.insert(
                ADDON_TABLE_NAME,
                NAME_FULL_ID,
                values
        );

    }
}
