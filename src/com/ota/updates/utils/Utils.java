/*
 * Copyright (C) 2014 Matt Booth (Kryten2k35).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ota.updates.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;

import com.ota.updates.RomUpdate;
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
				if(line.contains("[" + propName +"]")){
					valid = true;
				}
			}
			bufferedReader.close();
		} 
		catch (IOException e){
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
			while ((line=br.readLine()) != null){
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

		for (int i = 0; i < items.length; i++){
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
		Tools.noneRootShell("rm -f " + file.getAbsolutePath());
	}
	
	public static void setHasFileDownloaded(Context context) {
		File file = RomUpdate.getFullFile(context);
		int filesize = RomUpdate.getFileSize(context);
		
		boolean status = false;
		if(DEBUGGING)
			Log.d(TAG, "Local file " + file.getAbsolutePath());
			Log.d(TAG, "Local filesize " + file.length());
			Log.d(TAG, "Remote filesize " + filesize);
		if(file.length() != 0 && file.length() == filesize){
			status = true;
		}		
		Preferences.setDownloadFinished(context, status);
	}
	
	public static void setBackgroundCheck(Context context, boolean set){
        int requestedInteval = Preferences.getBackgroundFrequency(context);

        if(DEBUGGING)
			Log.d(TAG, "" + requestedInteval);
        
        Intent intent = new Intent(context, AppReceiver.class);
        intent.setAction(START_UPDATE_CHECK);
        
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis() + requestedInteval * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(set){
        	if(DEBUGGING)
				Log.d(TAG, "Alarm set for " + requestedInteval + " seconds");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, requestedInteval*1000, PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        } else {
        	if(DEBUGGING)
				Log.d(TAG, "Cancelling alarm");
            alarmManager.cancel(PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }
	
	public static boolean isConnected(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
	}
	
	public static boolean isMobileNetwork(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
	}
	
	public static boolean isLollipop(){	
		return Build.VERSION.SDK_INT >= 21;
	}
	
	private static boolean versionBiggerThan(String current, String manifest) {
		// returns true if current > manifest, false otherwise
		if (current.length() > manifest.length()) {
			for (int i = 0; i < current.length() - manifest.length(); i++) {
				manifest+="0";
			}
		} else if (manifest.length() > current.length()) {
			for (int i = 0; i < manifest.length() - current.length(); i++) {
				current+="0";
			}
		}

		for (int i = 0; i <= current.length(); i++) {
			if (current.charAt(i) > manifest.charAt(i)){
				return true; // definitely true
			} else if (manifest.charAt(i) > current.charAt(i)) {
				return false; // definitely false
			} else {
				//else still undecided
			}
		}
		return false;
	}

	public static void setUpdateAvailability(Context context) {
		// Grab the data from the device and manifest
		int otaVersion = RomUpdate.getVersionNumber(context);
		String currentVer = Utils.getProp("ro.ota.version");
		String manifestVer = Integer.toString(otaVersion);

		boolean available = !versionBiggerThan(currentVer, manifestVer);

		RomUpdate.setUpdateAvailable(context, available);
		if(DEBUGGING)
			Log.d(TAG, "Update Availability is " + available);
	}
}
