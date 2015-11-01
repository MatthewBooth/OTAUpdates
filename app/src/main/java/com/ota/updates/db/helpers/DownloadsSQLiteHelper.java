package com.ota.updates.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ota.updates.items.DownloadItem;

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
public class DownloadsSQLiteHelper extends BaseSQLiteHelper {

    public DownloadsSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Add a download entry into the database
     *
     * @param fileId     File id to add to the database
     * @param downloadId Download id to add to the database
     */
    public void addDownload(int fileId, long downloadId) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, fileId);
        values.put(NAME_DOWNLOAD_ID, (int) downloadId);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insertWithOnConflict(DOWNLOAD_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Remove a download entry from the database
     *
     * @param downloadId the id to remove
     */
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public void removeDownload(long downloadId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DOWNLOAD_TABLE_NAME, NAME_DOWNLOAD_ID + " = ?", new String[]{Long.toString(downloadId)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * Retrieve an entry from the database
     *
     * @param fileId the file id to retrieve
     * @return a DownloadItem
     */
    public DownloadItem getDownloadEntry(int fileId) {
        String query = "SELECT * FROM "
                + DOWNLOAD_TABLE_NAME
                + "WHERE "
                + NAME_ID
                + " = "
                + Integer.toString(fileId);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        DownloadItem downloadItem = new DownloadItem();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            downloadItem.setFileId(Integer.parseInt(cursor.getString(0)));
            downloadItem.setDownloadId(Long.parseLong(cursor.getString(1)));
            cursor.close();
        } else {
            downloadItem = null;
        }

        db.close();

        return downloadItem;
    }
}
