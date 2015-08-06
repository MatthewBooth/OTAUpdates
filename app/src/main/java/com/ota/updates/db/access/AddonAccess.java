package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;

import com.ota.updates.db.Contracts;
import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;

public class AddonAccess extends BaseAccess<AddonItem> {
    public AddonAccess(Context context) {
        mDbHelper = new AddonSQLiteHelper(context);
    }

    @Override
    public void put(AddonItem item) {
        ContentValues values = new ContentValues();
        values.put(Contracts.COLUMN_NAME_ID, item.getId());
        values.put(Contracts.COLUMN_NAME_DOWNLOADS, item.getDownloads());
        values.put(Contracts.COLUMN_NAME_NAME, item.getName());
        values.put(Contracts.COLUMN_NAME_SLUG, item.getSlug());
        values.put(Contracts.COLUMN_NAME_DESCRIPTION, item.getDescription());
        values.put(Contracts.COLUMN_NAME_UPDATED_AT, item.getUpdatedAt());
        values.put(Contracts.COLUMN_NAME_CREATED_AT, item.getCreatedAt());
        values.put(Contracts.COLUMN_NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(Contracts.COLUMN_NAME_SIZE, item.getSize());
        values.put(Contracts.COLUMN_NAME_MD5, item.getMd5());
        values.put(Contracts.COLUMN_NAME_DOWNLOAD_LINK, item.getDownloadLink());

        long newRowId;
        newRowId = mDatabase.insert(
                Contracts.AddonContract.TABLE_NAME,
                Contracts.COLUMN_NAME_FULL_ID,
                values
        );

    }
}
