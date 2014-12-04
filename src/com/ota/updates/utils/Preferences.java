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

import com.ota.updates.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences implements Constants{
    
    public static final String TAG = "Preferences";
    
    public static String PREF_NAME = "OTAUpdateSettings";
	
    private Preferences() {
    }
    
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

	public static String getUpdateLastChecked(Context context, String time){
        return getPrefs(context).getString(LAST_CHECKED, time);
    }
	
	public static boolean getDownloadFinished(Context context){
		return getPrefs(context).getBoolean(IS_DOWNLOAD_FINISHED, false);
	}
	
	public static String getDownloadLocation(Context context){
		String defLocation = context.getResources().getString(R.string.download_folder);
	    return getPrefs(context).getString(DOWNLOAD_LOC, defLocation);
	}
	
	public static boolean getDeleteAfterInstall(Context context){
		return getPrefs(context).getBoolean(DELETE_AFTER_INSTALL, false);
	}
	
	public static boolean getWipeData(Context context){
		return getPrefs(context).getBoolean(WIPE_DATA, false);
	}
	
	public static boolean getWipeCache(Context context){
		return getPrefs(context).getBoolean(WIPE_CACHE, true);
	}
	
	public static boolean getWipeDalvik(Context context){
		return getPrefs(context).getBoolean(WIPE_DALVIK, true);
	}
	
	public static boolean getMD5Passed(Context context){
		return getPrefs(context).getBoolean(MD5_PASSED, false);
	}
	
	public static boolean getHasMD5Run(Context context){
		return getPrefs(context).getBoolean(MD5_RUN, false);
	}
	
	public static boolean getIsDownloadOnGoing(Context context){
		return getPrefs(context).getBoolean(DOWNLOAD_RUNNING, false);
	}
	
	public static String getNetworkType(Context context){
		return getPrefs(context).getString(NETWORK_TYPE, "2");
	}
	
	public static long getDownloadID(Context context){
		return getPrefs(context).getLong(DOWNLOAD_ID, 0L);
	}
	
	public static boolean getBackgroundService(Context context){
	    return getPrefs(context).getBoolean(UPDATER_BACK_SERVICE, true);
	}
	
	public static int getBackgroundFrequency(Context context){
	    return Integer.parseInt(getPrefs(context).getString(UPDATER_BACK_FREQ, "259200"));
	}
	
	public static boolean getORSEnabled(Context context){
		Log.d(TAG, "ORS Enabled Preference " + getPrefs(context).getBoolean(UPDATER_ENABLE_ORS, false));
		return getPrefs(context).getBoolean(UPDATER_ENABLE_ORS, false);
	}
	
	public static int getCurrentTheme(Context context){
		return Integer.parseInt(getPrefs(context).getString(CURRENT_THEME, "2")); // #2 is the Dark Theme
	}
	
	public static int getTheme(Context context)
    {       
        switch(getCurrentTheme(context))
        {
        case 0:
            return R.style.Theme_RagnarLight;
        case 1:
            return R.style.Theme_RagnarLight_DarkActionBar;
        case 2:
            return R.style.Theme_RagnarDark;
        default:
        	if(Utils.isLollipop()) {
        		return R.style.Theme_RagnarLight;
        	} else {
        		return R.style.Theme_RagnarDark;
        	}
        }
    }
	
	public static int getSettingsTheme(Context context)
    {       
        switch(getCurrentTheme(context))
        {
        case 0:
            return R.style.Theme_RagnarLight;
        case 1:
            return R.style.Theme_RagnarLight_DarkActionBar;
        case 2:
            return R.style.Theme_RagnarDark_Settings;
        default:
        	if(Utils.isLollipop()) {
        		return R.style.Theme_RagnarLight_Settings;
        	} else {
        		return R.style.Theme_RagnarDark_Settings;
        	}
        }
    }

	public static void setUpdateLastChecked(Context context, String time){
	    SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(LAST_CHECKED, time);
        editor.commit();
	}
	
	public static void setDownloadFinished(Context context, boolean value){
	    SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(IS_DOWNLOAD_FINISHED, value);
        editor.commit();
	}
	
	public static void setDownloadLocation(Context context,String path){
	    SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(DOWNLOAD_LOC, path);
        editor.commit();
	}
	
	public static void setTheme(Context context, String value){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(CURRENT_THEME, value);
        editor.commit();
    }
	
	public static void setDeleteAfterInstall(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(DELETE_AFTER_INSTALL, value);
        editor.commit();
	}
	
	public static void setWipeData(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(WIPE_DATA, value);
        editor.commit();
	}
	
	public static void setWipeCache(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(WIPE_CACHE, value);
        editor.commit();
	}
	
	public static void setWipeDalvik(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(WIPE_DALVIK, value);
        editor.commit();
	}
	
	public static void setMD5Passed(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(MD5_PASSED, value);
        editor.commit();
	}
	
	public static void setHasMD5Run(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(MD5_RUN, value);
        editor.commit();
	}
	
	public static void setIsDownloadRunning(Context context, boolean value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(DOWNLOAD_RUNNING, value);
        editor.commit();
	}
	
	public static void setNetworkType(Context context, String value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(NETWORK_TYPE, value);
        editor.commit();
	}
	
	public static void setDownloadID(Context context, long value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putLong(DOWNLOAD_ID, value);
        editor.commit();
	}
	
	public static void setBackgroundFrequency(Context context, String value){
		SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(UPDATER_BACK_FREQ, value);
        editor.commit();
	}
}
