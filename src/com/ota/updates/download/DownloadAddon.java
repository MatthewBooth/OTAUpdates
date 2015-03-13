package com.ota.updates.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.ota.updates.OtaUpdates;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;

public class DownloadAddon implements Constants {

	public final static String TAG = "DownloadAddon";
	
	public DownloadAddon() {
		
	}
	
	public void startDownload(Context context, String url, String fileName, int id) {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		
		if(Preferences.getNetworkType(context).equals("2")) {
			// All network types are enabled by default
			// So if we choose Wi-Fi only, then enable the restriction
			request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		} 
		
		request.setTitle(fileName);

		request.setVisibleInDownloadsUi(true);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		fileName = fileName + ".zip";
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
		
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		long mDownloadID = downloadManager.enqueue(request);
		OtaUpdates.putAddonDownload(id, mDownloadID);
		new DownloadAddonProgress(context, downloadManager, id).execute(mDownloadID);
		if (DEBUGGING) {
			Log.d(TAG, "Starting download with manager ID " + mDownloadID + " and item id of " + id);
		}
	}
	
	public void cancelDownload(Context context, int id) {
		long mDownloadID = OtaUpdates.getAddonDownload(id);
		if (DEBUGGING) {
			Log.d(TAG, "Stopping download with manager ID " + mDownloadID + " and item id of " + id);
		}
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManager.remove(mDownloadID);
	}
}
