package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;

import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.items.UploadItem;

public class UploadAccess extends BaseAccess<UploadItem> {

    public UploadAccess(Context context) {
        mDbHelper = new UploadSQLiteHelper(context);
    }

    @Override
    public void put(UploadItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_SIZE, item.getSize());
        values.put(NAME_MD5, item.getMd5());
        values.put(NAME_STATUS, item.getStatus());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_DOWNLOAD_LINK, item.getDownloadLink());

        long newRowId;
        newRowId = mDatabase.insert(
                UPLOAD_TABLE_NAME,
                NAME_FULL_ID, // A nullable ID from another table...
                values
        );
    }
}
