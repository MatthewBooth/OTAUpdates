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

import android.os.Environment;

public interface Constants {
	// Developer
	public static final boolean DEBUGGING 								= false;
	public static final boolean DEBUG_NOTIFICATIONS 					= false;
	
	// Props
	public static final String OTA_ROMNAME 								= "ro.ota.romname";
	public static final String OTA_VERSION 								= "ro.ota.version";
	public static final String OTA_MANIFEST 							= "ro.ota.manifest";
	public static final String OTA_DOWNLOAD_LOC							= "ro.ota.download_loc";
	public static final String OTA_DEFAULT_THEME						= "ro.ota.default_theme";
	
	// Storage
	public static final String SD_CARD 									= Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String OTA_DOWNLOAD_DIR 						= Utils.doesPropExist(OTA_DOWNLOAD_LOC) ? Utils.getProp(OTA_DOWNLOAD_LOC) : "OTAUpdates";
	public static final String INSTALL_AFTER_FLASH_DIR 					= "InstallAfterFlash";

	// Networks
	public static final String WIFI_ONLY 								= "2";
	public static final String WIFI_OR_MOBILE 							= "1";

	// Theme
	public static final String THEME_LIGHT 								= "0";
	public static final String THEME_LIGHT_DARKACTIONBAR 				= "1";
	public static final String THEME_DARK 								= "2";

	// Settings
	public static final String CURRENT_THEME 							= "current_theme";
	public static final String LAST_CHECKED 							= "updater_last_update_check";
	public static final String IS_DOWNLOAD_FINISHED 					= "is_download_finished";
	public static final String DELETE_AFTER_INSTALL 					= "delete_after_install";
	public static final String INSTALL_PREFS 							= "install_prefs";
	public static final String WIPE_DATA 								= "wipe_data";
	public static final String WIPE_CACHE 								= "wipe_cache";
	public static final String WIPE_DALVIK 								= "wipe_dalvik";
	public static final String MD5_PASSED 								= "md5_passed";
	public static final String MD5_RUN 									= "md5_run";
	public static final String DOWNLOAD_RUNNING		 					= "download_running";
	public static final String NETWORK_TYPE 							= "network_type";
	public static final String DOWNLOAD_ID 								= "download_id";
	public static final String UPDATER_BACK_SERVICE 					= "background_service";
	public static final String UPDATER_BACK_FREQ 						= "background_frequency";
	public static final String UPDATER_ENABLE_ORS 						= "updater_twrp_ors";
	public static final String MOVE_TO_EXT_SD 							= "move_to_ext_sd";
	public static final String NOTIFICATIONS_SOUND 						= "notifications_sound";
	public static final String NOTIFICATIONS_VIBRATE 					= "notifications_vibrate";
	public static final String IGNORE_RELEASE_VERSION 					= "ignored_release";
	public static final String ADS_ENABLED 								= "ads_enabled";
	public static final String OLD_CHANGELOG 							= "old_changelog";
	public static final String FIRST_RUN 								= "first_run";
	public static final String ABOUT_ACTIVITY_PREF						= "about_activity_pref";
	public static final String IS_PRO									= "is_pro";
	public static final String ABOUT_PREF_PRO 							= "about_pro";
	public static final String STORAGE_LOCATION 						= "updater_storage_location";

	// Broadcast intents
	public static final String MANIFEST_LOADED 							= "com.ota.update.MANIFEST_LOADED";
	public static final String MANIFEST_CHECK_BACKGROUND 				= "com.ota.update.MANIFEST_CHECK_BACKGROUND";
	public static final String START_UPDATE_CHECK 						= "com.ota.update.START_UPDATE_CHECK";
	public static final String IGNORE_RELEASE 							= "com.ota.update.IGNORE_RELEASE";

	//Notification
	public static final int NOTIFICATION_ID 							= 101;
}
