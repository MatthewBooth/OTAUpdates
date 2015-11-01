package com.ota.updates.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.ota.updates.callbacks.DownloadComplete;
import com.ota.updates.db.helpers.DownloadsSQLiteHelper;
import com.ota.updates.items.DownloadItem;
import com.ota.updates.utils.Constants;

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
public class DownloadReceiver extends BroadcastReceiver implements Constants {

    public final String TAG = this.getClass().getSimpleName();

    private DownloadComplete mDownloadComplete;

    public DownloadReceiver(DownloadComplete downloadComplete) {
        mDownloadComplete = downloadComplete;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);

            DownloadsSQLiteHelper helper = new DownloadsSQLiteHelper(context);
            DownloadItem downloadItem = helper.getDownloadEntry(id);

            if (downloadItem == null) {
                if (DEBUGGING)
                    Log.v(TAG, "Ignoring unrelated download " + id);
                return;
            }

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);

            // it shouldn't be empty, but just in case
            if (!cursor.moveToFirst()) {
                if (DEBUGGING)
                    Log.e(TAG, "Empty row");
                return;
            }

            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
                if (DEBUGGING) {
                    Log.w(TAG, "Download Failed");
                }
                mDownloadComplete.downloadFailed();

                return;
            } else {
                if (DEBUGGING) {
                    Log.v(TAG, "Download Succeeded");
                }
                mDownloadComplete.downloadSuccessful();

                return;
            }
        }
    }
}
