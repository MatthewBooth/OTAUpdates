package com.ota.updates.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ota.updates.items.DownloadItem;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    private static DownloadsSQLiteHelper mInstance = null;

    public static synchronized DownloadsSQLiteHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DownloadsSQLiteHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public DownloadsSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Add a download entry into the database
     *
     * @param fileId     File id to add to the database
     * @param downloadId Download id to add to the database
     * @param downloadType  The type of download to store
     */
    public synchronized void addDownload(int fileId, long downloadId, int downloadType, Timestamp downloadStarted) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, fileId);
        values.put(NAME_DOWNLOAD_ID, (int) downloadId);
        values.put(NAME_DOWNLOAD_TYPE, downloadType);
        values.put(NAME_DOWNLOAD_STARTED, downloadStarted.toString());
        values.put(NAME_DOWNLOAD_FINISHED, new Timestamp(System.currentTimeMillis() - 100000).toString());
        values.put(NAME_DOWNLOAD_STATUS, DOWNLOAD_STATUS_RUNNING);

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        Long result = db.insertWithOnConflict(DOWNLOAD_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (result != -1) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    public synchronized void updateFinishedDownload(int fileId, Timestamp downloadFinished, Integer status) {
        ContentValues values = new ContentValues();
        values.put(NAME_DOWNLOAD_FINISHED, downloadFinished.toString());
        values.put(NAME_DOWNLOAD_STATUS, status);

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        db.update(DOWNLOAD_TABLE_NAME, values, NAME_ID + " = ?", new String[]{String.valueOf(fileId)});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Remove a download entry from the database
     *
     * @param downloadId the id to remove
     */
    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public synchronized void removeDownload(long downloadId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DOWNLOAD_TABLE_NAME, NAME_DOWNLOAD_ID + " = ?", new String[]{Long.toString(downloadId)});
            if (DEBUGGING) {
                Log.d(this.getClass().getName(), "Download with ID " + downloadId + " removed");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Retrieve an entry from the database
     *
     * @param fileId the file id to retrieve
     * @return a DownloadItem
     */
    public synchronized DownloadItem getDownloadEntryByFileId(int fileId) {
        String query = "SELECT * FROM "
                + DOWNLOAD_TABLE_NAME
                + " WHERE "
                + NAME_ID
                + " = "
                + Integer.toString(fileId);

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);

        DownloadItem downloadItem;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            downloadItem = getDownloadItemFromCursor(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } else {
            downloadItem = null;
        }

        db.endTransaction();

        return downloadItem;
    }

    /**
     * Retrieve an entry from the database
     *
     * @param downloadId the download id to retrieve
     * @return a DownloadItem
     */
    public synchronized DownloadItem getDownloadEntryByDownloadId(long downloadId) {
        String query = "SELECT * FROM "
                + DOWNLOAD_TABLE_NAME
                + " WHERE "
                + NAME_DOWNLOAD_ID
                + " = "
                + Long.toString(downloadId);

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);

        DownloadItem downloadItem;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            downloadItem = getDownloadItemFromCursor(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } else {
            downloadItem = null;
        }

        db.endTransaction();

        return downloadItem;
    }

    public synchronized ArrayList<DownloadItem> getListOfDownloads() {
        ArrayList<DownloadItem> list = new ArrayList<>();

        Cursor cursor = getAllEntries(DOWNLOAD_TABLE_NAME);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                DownloadItem downloadItem = getDownloadItemFromCursor(cursor);
                list.add(downloadItem);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    @NonNull
    private synchronized DownloadItem getDownloadItemFromCursor(Cursor cursor) {
        DownloadItem downloadItem = new DownloadItem();
        downloadItem.setFileId(Integer.parseInt(cursor.getString(0)));
        downloadItem.setDownloadId(cursor.getLong(1));
        downloadItem.setDownloadType(Integer.parseInt(cursor.getString(2)));
        downloadItem.setDownloadStarted(Timestamp.valueOf(cursor.getString(3)));
        downloadItem.setDownloadFinished(Timestamp.valueOf(cursor.getString(4)));
        downloadItem.setDownloadStatus(Integer.parseInt(cursor.getString(5)));
        return downloadItem;
    }
}
