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

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.ota.updates.R;

public class Preferences implements Constants{

	public static final String TAG = "Preferences";

	public static String PREF_NAME = "OTAUpdateSettings";

	private Preferences() {
	}

	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}

	public static String getUpdateLastChecked(Context context, String time) {
		return getPrefs(context).getString(LAST_CHECKED, time);
	}

	public static boolean getDownloadFinished(Context context) {
		return getPrefs(context).getBoolean(IS_DOWNLOAD_FINISHED, false);
	}

	public static boolean getDeleteAfterInstall(Context context) {
		return getPrefs(context).getBoolean(DELETE_AFTER_INSTALL, false);
	}

	public static boolean getWipeData(Context context) {
		return getPrefs(context).getBoolean(WIPE_DATA, false);
	}

	public static boolean getWipeCache(Context context) {
		return getPrefs(context).getBoolean(WIPE_CACHE, true);
	}

	public static boolean getWipeDalvik(Context context) {
		return getPrefs(context).getBoolean(WIPE_DALVIK, true);
	}

	public static boolean getMD5Passed(Context context) {
		return getPrefs(context).getBoolean(MD5_PASSED, false);
	}

	public static boolean getHasMD5Run(Context context) {
		return getPrefs(context).getBoolean(MD5_RUN, false);
	}

	public static boolean getIsDownloadOnGoing(Context context) {
		return getPrefs(context).getBoolean(DOWNLOAD_RUNNING, false);
	}

	public static String getNetworkType(Context context) {
		return getPrefs(context).getString(NETWORK_TYPE, WIFI_ONLY);
	}

	public static long getDownloadID(Context context) {
		return getPrefs(context).getLong(DOWNLOAD_ID, 0L);
	}

	public static String getNotificationSound(Context context) {
		String defValue = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString();
		return getPrefs(context).getString(NOTIFICATIONS_SOUND, defValue);
	}

	public static Boolean getNotificationVibrate(Context context) {
		return getPrefs(context).getBoolean(NOTIFICATIONS_VIBRATE, true);
	}

	public static boolean getBackgroundService(Context context) {
		if (DEBUGGING)
			Log.d(TAG, "Background Service set to " + getPrefs(context).getBoolean(UPDATER_BACK_SERVICE, true));
		return getPrefs(context).getBoolean(UPDATER_BACK_SERVICE, true);
	}

	public static int getBackgroundFrequency(Context context) {
		return Integer.parseInt(getPrefs(context).getString(UPDATER_BACK_FREQ, "43200"));
	}

	public static boolean getORSEnabled(Context context) {
		Log.d(TAG, "ORS Enabled Preference " + getPrefs(context).getBoolean(UPDATER_ENABLE_ORS, false));
		return getPrefs(context).getBoolean(UPDATER_ENABLE_ORS, false);
	}

	public static int getCurrentTheme(Context context) {
		Boolean isDefaultThemeUsed = Utils.doesPropExist(OTA_DEFAULT_THEME);
		String getDefTheme = Utils.getProp(OTA_DEFAULT_THEME);
		Boolean isLollipop = Utils.isLollipop();

		// Has a a default theme been set by the developer?
		if(isDefaultThemeUsed && !getDefTheme.isEmpty()) {
			int defThemeInt = Integer.parseInt(getDefTheme);
			if(!(defThemeInt < 0 || defThemeInt > 2)) {
				return Integer.parseInt(getPrefs(context).getString(CURRENT_THEME, getDefTheme));
			} else {
				return normalTheme(context, isLollipop);
			}
		} else {
			return normalTheme(context, isLollipop);
		}
	}

	private static int normalTheme(Context context, Boolean isLollipop) {
		if (isLollipop) {
			return Integer.parseInt(getPrefs(context).getString(CURRENT_THEME, THEME_LIGHT));
		} else {
			return Integer.parseInt(getPrefs(context).getString(CURRENT_THEME, THEME_DARK));
		}
	}

	public static int getTheme(Context context) {
		boolean isLollipop = Utils.isLollipop();
		switch(getCurrentTheme(context))
		{
		case 0:
			return R.style.Theme_RagnarLight;
		case 1:
			// Lollipop doesn't have a DarkActionBar theme
			if (isLollipop) {
				return R.style.Theme_RagnarLight;
			} else {
				return R.style.Theme_RagnarLight_DarkActionBar;
			}
		case 2:
			return R.style.Theme_RagnarDark;
		default:
			if (isLollipop) {
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
            return R.style.Theme_RagnarLight_Settings;
        case 1:
            return R.style.Theme_RagnarLight_DarkActionBar_Settings;
        case 2:
            return R.style.Theme_RagnarDark_Settings;
        default:
        	if (Utils.isLollipop()) {
        		return R.style.Theme_RagnarLight_Settings;
        	} else {
        		return R.style.Theme_RagnarDark_Settings;
        	}
        }
    }

	public static String getIgnoredRelease(Context context) {
		return getPrefs(context).getString(IGNORE_RELEASE_VERSION, "0");
	}

	public static Boolean getAdsEnabled(Context context) {
		return getPrefs(context).getBoolean(ADS_ENABLED, true);
	}

	public static String getOldChangelog(Context context) {
		return getPrefs(context).getString(OLD_CHANGELOG, context.getResources().getString(R.string.app_version));
	}

	public static Boolean getFirstRun(Context context) {
		return getPrefs(context).getBoolean(FIRST_RUN, true);
	}
	
	public static Boolean getIsPro(Context context) {
		return getPrefs(context).getBoolean(IS_PRO, false);
	}

	public static void setUpdateLastChecked(Context context, String time) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(LAST_CHECKED, time);
		editor.commit();
	}

	public static void setDownloadFinished(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(IS_DOWNLOAD_FINISHED, value);
		editor.commit();
	}

	public static void setTheme(Context context, String value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(CURRENT_THEME, value);
		editor.commit();
	}

	public static void setDeleteAfterInstall(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(DELETE_AFTER_INSTALL, value);
		editor.commit();
	}

	public static void setWipeData(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(WIPE_DATA, value);
		editor.commit();
	}

	public static void setWipeCache(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(WIPE_CACHE, value);
		editor.commit();
	}

	public static void setWipeDalvik(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(WIPE_DALVIK, value);
		editor.commit();
	}

	public static void setMD5Passed(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(MD5_PASSED, value);
		editor.commit();
	}

	public static void setHasMD5Run(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(MD5_RUN, value);
		editor.commit();
	}

	public static void setIsDownloadRunning(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(DOWNLOAD_RUNNING, value);
		editor.commit();
	}

	public static void setDownloadID(Context context, long value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putLong(DOWNLOAD_ID, value);
		editor.commit();
	}

	public static void setBackgroundFrequency(Context context, String value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(UPDATER_BACK_FREQ, value);
		editor.commit();
	}

	public static void setIgnoredRelease(Context context, String value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(IGNORE_RELEASE_VERSION, value);
		editor.commit();
	}

	public static void setOldChangelog(Context context, String value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putString(OLD_CHANGELOG, value);
		editor.commit();
	}

	public static void setFirstRun(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(FIRST_RUN, value);
		editor.commit();
	}
	
	public static void setIsPro(Context context, boolean value) {
		SharedPreferences.Editor editor = getPrefs(context).edit();
		editor.putBoolean(IS_PRO, value);
		editor.commit();
	}
}