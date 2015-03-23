/*
 * Copyright (C) 2015 Matt Booth (Kryten2k35).
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

package com.ota.updates.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;

import com.ota.updates.R;
import com.ota.updates.RomUpdate;
import com.ota.updates.activities.AvailableActivity;
import com.ota.updates.activities.MainActivity;
import com.ota.updates.receivers.AppReceiver;

public class Utils implements Constants{

	public final static String TAG = "Utils";

	private static final String EOL = System.getProperty("line.separator");

	private static final int KILOBYTE = 1024;
	private static int KB = KILOBYTE;
	private static int MB = KB * KB;
	private static int GB = MB * KB;

	private static DecimalFormat decimalFormat = new DecimalFormat("##0.#");
	static {
		decimalFormat.setMaximumIntegerDigits(3);
		decimalFormat.setMaximumFractionDigits(1);
	}

	public static Boolean doesPropExist(String propName) {
		boolean valid = false;

		try {
			Process process = Runtime.getRuntime().exec("getprop");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
				if (line.contains("[" + propName +"]")) {
					valid = true;
				}
			}
			bufferedReader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return valid;
	}

	public static String getProp(String propName) {
		Process p = null;
		String result = "";
		try {
			p = new ProcessBuilder("/system/bin/getprop", propName).redirectErrorStream(true).start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line=br.readLine()) != null) {
				result = line;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressLint("NewApi")
	static public CharSequence getBulletList(String header, String []items) {
		CharSequence output = "";

		if (header != null && !header.isEmpty()) {
			output = header + EOL + EOL;
		}

		for (int i = 0; i < items.length; i++) {
			String text = items[i];
			SpannableString span = new SpannableString(text + EOL + EOL);
			span.setSpan(new BulletSpan(20), 0, text.length(), 0);          
			output = TextUtils.concat(output, span);         
		}   	
		return output;
	}

	public static String formatDataFromBytes(long size) {

		String symbol;
		KB = KILOBYTE;
		symbol = "B";
		if (size < KB) {
			return decimalFormat.format(size) + symbol;
		} else if (size < MB) {
			return decimalFormat.format(size / (float)KB) + 'k' + symbol;
		} else if (size < GB) {
			return decimalFormat.format(size / (float)MB) + 'M' + symbol;
		}
		return decimalFormat.format(size / (float)GB) + 'G' + symbol;
	}

	public static void deleteFile(File file) {
		Tools.shell("rm -f " + file.getAbsolutePath(), false);
	}

	public static void setHasFileDownloaded(Context context) {
		File file = RomUpdate.getFullFile(context);
		int filesize = RomUpdate.getFileSize(context);
		boolean downloadIsRunning = Preferences.getIsDownloadOnGoing(context);

		boolean status = false;
		if (DEBUGGING) {
			Log.d(TAG, "Local file " + file.getAbsolutePath());
			Log.d(TAG, "Local filesize " + file.length());
			Log.d(TAG, "Remote filesize " + filesize);
		}
		if (file.length() != 0 && file.length() == filesize && !downloadIsRunning) {
			status = true;
		}		
		Preferences.setDownloadFinished(context, status);
	}

	public static void setBackgroundCheck(Context context, boolean set) {
		scheduleNotification(context, !set);
	}
	
	public static void scheduleNotification(Context context, boolean cancel) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AppReceiver.class);
		intent.setAction(START_UPDATE_CHECK);
		int intentId = 1673;
		int intentFlag = PendingIntent.FLAG_UPDATE_CURRENT;
		
		if (cancel) {
			if (alarmManager != null) {
				if (DEBUGGING)
					Log.d(TAG, "Cancelling alarm");
				alarmManager.cancel(PendingIntent.getBroadcast(
						context, 
						intentId, 
						intent, 
						intentFlag));
			}
		} else {
			int requestedInterval;
			
			if (DEBUG_NOTIFICATIONS) {
				requestedInterval = 30;
			} else {
				requestedInterval = Preferences.getBackgroundFrequency(context);
			}
			
			if (DEBUGGING)
				Log.d(TAG, "Setting alarm for " + requestedInterval + " seconds");
			Calendar calendar = Calendar.getInstance();
			long time = calendar.getTimeInMillis() + requestedInterval * 1000;
			alarmManager.set(
					AlarmManager.RTC_WAKEUP, 
					time, 
					PendingIntent.getBroadcast(
							context, 
							intentId, 
							intent, 
							intentFlag));
		}
	}


	public static boolean isConnected(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();
	}

	public static boolean isMobileNetwork(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	public static boolean isLollipop() {	
		return Build.VERSION.SDK_INT >= 21;
	}

	private static boolean versionBiggerThan(String current, String manifest) {
		// returns true if current > manifest, false otherwise
		if (current.length() > manifest.length()) {
			for (int i = 0; i < current.length() - manifest.length(); i++) {
				manifest += "0";
			}
		} else if (manifest.length() > current.length()) {
			for (int i = 0; i < manifest.length() - current.length(); i++) {
				current += "0";
			}
		}
		
		if (DEBUGGING)
			Log.d(TAG, "Current: " + current + " Manifest: " + manifest);

		return Integer.parseInt(current) < Integer.parseInt(manifest);
	}

    public static boolean isUpdateIgnored(Context context) {
        String manifestVer = Integer.toString(RomUpdate.getVersionNumber(context));
        return Preferences.getIgnoredRelease(context).matches(manifestVer);
    }

	public static void setUpdateAvailability(Context context) {
		// Grab the data from the device and manifest
		int otaVersion = RomUpdate.getVersionNumber(context);
		String currentVer = Utils.getProp("ro.ota.version");
		String manifestVer = Integer.toString(otaVersion);

        boolean available;
        if (Preferences.getIgnoredRelease(context).matches(manifestVer)) {
            available = false;
        } else {
            available = DEBUG_NOTIFICATIONS ? true : versionBiggerThan(currentVer, manifestVer);
        }

		RomUpdate.setUpdateAvailable(context, available);
		if (DEBUGGING)
			Log.d(TAG, "Update Availability is " + available);
	}
	
	public static void setupNotification(Context context, String filename) {
		if (DEBUGGING)
			Log.d(TAG, "Showing notification");	
		
		NotificationManager mNotifyManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		Builder mBuilder = new NotificationCompat.Builder(context);
		Intent resultIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
        Intent skipIntent = new Intent(context, AppReceiver.class);
        skipIntent.setAction(IGNORE_RELEASE);
        Intent downloadIntent = new Intent(context, AvailableActivity.class);
        PendingIntent skipPendingIntent = PendingIntent.getBroadcast(context, 0, skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent downloadPendingIntent = PendingIntent.getActivity(context, 0, downloadIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle(context.getString(R.string.update_available))
		.setContentText(filename)
		.setSmallIcon(R.drawable.ic_notif)
		.setContentIntent(resultPendingIntent)
		.setAutoCancel(true)
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.setDefaults(NotificationCompat.DEFAULT_LIGHTS)
		.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
		.setSound(Uri.parse(Preferences.getNotificationSound(context)))
		.addAction(R.drawable.ic_action_download, context.getString(R.string.download), downloadPendingIntent)
        .addAction(R.drawable.ic_action_close, context.getString(R.string.ignore), skipPendingIntent);

		if (Preferences.getNotificationVibrate(context)) {
			mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
		}

		mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	public static boolean isAosp() {
		String prop = "com.ota.aosp";
		if(doesPropExist(prop)) {
			return getProp(prop).equalsIgnoreCase("true");
		} else {
			return false;
		}
	}
}
