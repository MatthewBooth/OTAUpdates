package com.ota.updates.db.access;

import android.content.ContentValues;
import android.content.Context;

import com.ota.updates.db.Contracts;
import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.items.UploadItem;

public class UploadAccess extends BaseAccess<UploadItem> {

    public UploadAccess(Context context) {
        mDbHelper = new UploadSQLiteHelper(context);
    }

    @Override
    public void put(UploadItem item) {
        ContentValues values = new ContentValues();
        values.put(Contracts.UploadContract.COLUMN_NAME_ID, item.getId());
        values.put(Contracts.UploadContract.COLUMN_NAME_SIZE, item.getSize());
        values.put(Contracts.UploadContract.COLUMN_NAME_MD5, item.getMd5());
        values.put(Contracts.UploadContract.COLUMN_NAME_STATUS, item.getStatus());
        values.put(Contracts.UploadContract.COLUMN_NAME_DOWNLOADS, item.getDownloads());
        values.put(Contracts.UploadContract.COLUMN_NAME_DOWNLOAD_LINK, item.getDownloadLink());

        long newRowId;
        newRowId = mDatabase.insert(
                Contracts.UploadContract.TABLE_NAME,
                Contracts.VersionContract.COLUMN_NAME_FULL_ID, // A nullable ID from another table...
                values
        );
    }
}
