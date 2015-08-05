package com.ota.updates.db.access;

import android.content.Context;

import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.items.UploadItem;

public class UploadAccess extends BaseAccess<UploadItem> {

    public UploadAccess(Context context) {
        mDbHelper = new UploadSQLiteHelper(context);
    }
    @Override
    public void put(UploadItem item) {

    }
}
