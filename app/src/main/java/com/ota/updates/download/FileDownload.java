package com.ota.updates.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.ota.updates.R;
import com.ota.updates.db.helpers.DownloadsSQLiteHelper;
import com.ota.updates.items.DownloadItem;
import com.ota.updates.utils.constants.App;

import java.sql.Timestamp;

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
public class FileDownload implements App {

    public FileDownload() {

    }

    public static Long startDownload(Context context, String url, String fileName, int fileId, Integer downloadType) {
        String description = context.getResources().getString(R.string.downloading);

        String zipExtension = ".zip";
        if (!fileName.contains(zipExtension)) {
            fileName = fileName + zipExtension;
        }

        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(url));

        downloadRequest.setTitle(fileName);
        downloadRequest.setDescription(description);

        downloadRequest.setVisibleInDownloadsUi(true);
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadRequest.setDestinationInExternalPublicDir(OTA_DOWNLOAD_DIR, fileName);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(downloadRequest);

        DownloadsSQLiteHelper downloadsSQLiteHelper = new DownloadsSQLiteHelper(context);
        downloadsSQLiteHelper.addDownload(fileId, downloadId, downloadType, new Timestamp(System.currentTimeMillis()));

        return downloadId;
    }

    public static void stopDownload(Context context, int fileId) {
        DownloadsSQLiteHelper downloadsSQLiteHelper = new DownloadsSQLiteHelper(context);
        DownloadItem downloadItem = downloadsSQLiteHelper.getDownloadEntryByFileId(fileId);
        long downloadId = downloadItem.getDownloadId();

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.remove(downloadId);

        downloadsSQLiteHelper.removeDownload(downloadId);
    }
}
