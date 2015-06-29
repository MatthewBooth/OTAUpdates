package com.ota.updates.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.ota.updates.OtaUpdates;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;

public class DownloadAddon implements Constants {

	public final static String TAG = "DownloadAddon";
	
	public DownloadAddon() {
		
	}
	
	public void startDownload(Context context, String url, String fileName, int id, int index) {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		
		if(Preferences.getNetworkType(context).equals(WIFI_ONLY)) {
			// All network types are enabled by default
			// So if we choose Wi-Fi only, then enable the restriction
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		} 
		
		request.setTitle(fileName);

		request.setVisibleInDownloadsUi(true);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		fileName = fileName + ".zip";
		request.setDestinationInExternalPublicDir(OTA_DOWNLOAD_DIR, fileName);
		
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		long mDownloadID = downloadManager.enqueue(request);
		OtaUpdates.putAddonDownload(index, mDownloadID);
		new DownloadAddonProgress(context, downloadManager, index).execute(mDownloadID);
		if (DEBUGGING) {
			Log.d(TAG, "Starting download with manager ID " + mDownloadID + " and item id of " + id);
		}
	}
	
	public void cancelDownload(Context context, int index) {
		long mDownloadID = OtaUpdates.getAddonDownload(index);
		if (DEBUGGING) {
			Log.d(TAG, "Stopping download with manager ID " + mDownloadID + " and item index of " + index);
		}
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManager.remove(mDownloadID);
	}
}
