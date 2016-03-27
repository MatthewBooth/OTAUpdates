package com.ota.updates.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.ota.updates.db.helpers.DownloadsSQLiteHelper;
import com.ota.updates.items.DownloadItem;
import com.ota.updates.utils.constants.App;
import com.ota.updates.utils.constants.BroadcastActions;

import java.sql.Timestamp;

public class DownloadReceiver extends BroadcastReceiver implements App {

    private static String TAG = DownloadReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();

        DownloadsSQLiteHelper downloadsSQLiteHelper = new DownloadsSQLiteHelper(context);

        // Handle the download completion by checking if it's one of ours, then checking the
        // completion status and sending a broadcast
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
            if (downloadsSQLiteHelper.getDownloadEntryByDownloadId(id) != null) {
                downloadCompleteHandling(context, downloadManager, query, downloadsSQLiteHelper, id);
            } else {
                if (DEBUGGING)
                    Log.v(TAG, "Ignoring unrelated non-ROM download " + id);
            }
        }
    }

    private void downloadCompleteHandling(Context context, DownloadManager downloadManager, DownloadManager.Query query, DownloadsSQLiteHelper downloadsSQLiteHelper, long id) {
        // Limit our query to the provided ID
        query.setFilterById(id);

        // Move the cursor to our Query results
        Cursor cursor = downloadManager.query(query);

        // It shouldn't be empty, but just in case
        if (!cursor.moveToFirst()) {
            if (DEBUGGING)
                Log.e(TAG, "Addon Download Empty row");
            return;
        }

        // Get the downloadEndStatus
        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

        // Get the intent ready, we'll set the action later
        Intent intent = new Intent();
        intent.putExtra("downloadId", id);

        // Make sure we won't be hitting a null exception getting the fileId
        DownloadItem downloadItem = downloadsSQLiteHelper.getDownloadEntryByDownloadId(id);
        if (downloadItem != null) {
            intent.putExtra("fileId", downloadItem.getFileId());
        }

        Integer downloadEndStatus;

        // Failure?
        if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
            if (DEBUGGING) {
                Log.w(TAG, "Download Failed");
            }
            intent.setAction(BroadcastActions.FAILED_DOWNLOAD);
            context.sendBroadcast(intent);
            downloadEndStatus = DOWNLOAD_STATUS_FAILED;
        }
        // Success?
        else {
            if (DEBUGGING) {
                Log.v(TAG, "Download Succeeded");
            }
            intent.setAction(BroadcastActions.SUCCESSFUL_DOWNLOAD);
            context.sendBroadcast(intent);
            downloadEndStatus = DOWNLOAD_STATUS_FINISHED;
        }

        // Update the database
        if (downloadItem != null) {
            downloadsSQLiteHelper.updateFinishedDownload(downloadItem.getFileId(),
                    new Timestamp(System.currentTimeMillis()), downloadEndStatus);
        } else {
            // This might be an error, so remove it from the database
            downloadsSQLiteHelper.removeDownload(id);
        }
    }
}