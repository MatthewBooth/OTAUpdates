package com.ota.updates.db.helpers;
/*
 * Copyright (C) 2015 Matt Booth.
 *
 * Licensed under the Attribution-NonCommercial-ShareAlike 4.0 International
 * (the "License") you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.ota.updates.items.UploadItem;

public class UploadSQLiteHelper extends BaseSQLiteHelper {

    public UploadSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Adds an UploadItem to the database
     * @param item  the UploadItem to be added
     */
    public void addUpload(UploadItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_SIZE, item.getSize());
        values.put(NAME_MD5, item.getMd5());
        values.put(NAME_STATUS, item.getStatus());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_DOWNLOAD_LINK, item.getDownloadLink());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insertWithOnConflict(UPLOAD_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Get a single UploadItem from the database
     * @param id  the ID of the item to be retrieved
     * @return the selected UploadItem
     */
    public UploadItem getUpload(int id) {
        String query = "SELECT * FROM " + UPLOAD_TABLE_NAME + " WHERE " + NAME_ID + " =  \"" + id + "\"";
        return getUploadItem(query);
    }

    /**
     * Gets the lastest UploadItem in the database
     * @return the UploadItem item that was requested
     */
    public UploadItem getLastUpload() {
        String query = "SELECT * FROM " + UPLOAD_TABLE_NAME + " ORDER BY " + NAME_ID + " DESC LIMIT 1";
        return getUploadItem(query);
    }

    /**
     * Runs a query on the Upload table in the database
     * @param query  The query to be executed
     * @return The resulting UploadItem
     */
    @Nullable
    private UploadItem getUploadItem(String query) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        UploadItem uploadItem = new UploadItem();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            uploadItem.setId(Integer.parseInt(cursor.getString(0)));
            uploadItem.setSize(Integer.parseInt(cursor.getString(1)));
            uploadItem.setMd5(cursor.getString(2));
            uploadItem.setStatus(cursor.getString(3));
            uploadItem.setDownloads(Integer.parseInt(cursor.getString(4)));
            uploadItem.setDownloadLink(cursor.getString(5));
            cursor.close();
        } else {
            uploadItem = null;
        }
        db.close();
        return uploadItem;
    }
}
