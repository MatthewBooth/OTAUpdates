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

package com.ota.updates;


import java.io.File;

import com.ota.updates.R;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class RomUpdate implements Constants{
    
    public final String TAG = this.getClass().getSimpleName();
    
    private static final String PREF_NAME = "ROMUpdate";

    private static String NAME = "rom_name";
    private static String VERSION = "rom_version";
    private static String CODENAME = "rom_codename";
    private static String DIRECT_URL = "rom_direct_url";
    private static String HTTP_URL = "rom_http_url";
    private static String MD5 = "rom_md5";
    private static String CHANGELOG = "rom_changelog";
    private static String ANDROID = "rom_android_ver";
    private static String WEBSITE = "rom_website";
    private static String DEVELOPER = "rom_developer";
    private static String DONATE_LINK = "rom_donate_link";
    private static String FILESIZE = "rom_filesize";
    private static String AVAILABILITY = "update_availability";
    
    private static String DEF_VALUE = "null";

    public RomUpdate() {
    }
    
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getName(Context context){
        return getPrefs(context).getString(NAME, DEF_VALUE);
    }
    
    public static String getVersion(Context context){
        return getPrefs(context).getString(VERSION, DEF_VALUE);
    }
    
    public static String getCodename(Context context){
    	return getPrefs(context).getString(CODENAME, DEF_VALUE);
    }
    
    public static String getDirectUrl(Context context){
    	return getPrefs(context).getString(DIRECT_URL, DEF_VALUE);
    }
    
    public static String getHttpUrl(Context context){
    	return getPrefs(context).getString(HTTP_URL, DEF_VALUE);
    }
    
    public static String getMd5(Context context){
    	return getPrefs(context).getString(MD5, DEF_VALUE);
    }
    
    public static String getChangelog(Context context){
    	return getPrefs(context).getString(CHANGELOG, DEF_VALUE);
    }
    
    public static String getAndroidVersion(Context context){
    	return getPrefs(context).getString(ANDROID, DEF_VALUE);
    }
    
    public static String getWebsite(Context context){
    	return getPrefs(context).getString(WEBSITE, DEF_VALUE);
    }
    
    public static String getDeveloper(Context context){
    	return getPrefs(context).getString(DEVELOPER, DEF_VALUE);
    }
    
    public static String getDonateLink(Context context){
    	return getPrefs(context).getString(DONATE_LINK, DEF_VALUE);
    }
    
    public static int getFileSize(Context context){
    	return getPrefs(context).getInt(FILESIZE, 0);
    }
    
    public static boolean getUpdateAvailability(Context context){
    	return getPrefs(context).getBoolean(AVAILABILITY, false);
    }
    
    public static void setName(Context context, String name){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(NAME, name);
        editor.commit();
    }
    
    public static void setVersion(Context context, String version){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(VERSION, version);
        editor.commit();
    }
    
    public static void setCodename(Context context, String codename){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(CODENAME, codename);
        editor.commit();
    }
    
    public static void setDirectUrl(Context context, String url){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(DIRECT_URL, url);
        editor.commit();
    }
    
    public static void setHttpUrl(Context context, String url){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(HTTP_URL, url);
        editor.commit();
    }
    
    public static void setMd5(Context context, String md5){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(MD5, md5);
        editor.commit();
    }
    
    public static void setChangelog(Context context, String change){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(CHANGELOG, change);
        editor.commit();
    }
    
    public static void setAndroidVersion(Context context, String android){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(ANDROID, android);
        editor.commit();
    }
    
    public static void setWebsite(Context context, String website){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(WEBSITE, website);
        editor.commit();
    }
    
    public static void setDeveloper(Context context, String developer){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(DEVELOPER, developer);
        editor.commit();
    }
    
    public static void setDonateLink(Context context, String donateLink){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(DONATE_LINK, donateLink);
        editor.commit();
    }
    
    public static void setFileSize(Context context, int size){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(FILESIZE, size);
        editor.commit();
    }
    
    public static void setUpdateAvailable(Context context, boolean availability){
    	SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(AVAILABILITY, availability);
        editor.commit();
    }
    
    public static String getFilename(Context context){
    	String filenameSeparator = context.getResources().getString(R.string.filename_separator);
		boolean isCodenameUsed = !RomUpdate.getCodename(context).equals("null");
		
		String codenameDevice = (isCodenameUsed ? 
				RomUpdate.getCodename(context) 
				+ filenameSeparator 
				+ Utils.getProp("ro.ota.device") 
				: Utils.getProp("ro.ota.device"));
		
    	String result = getName(context) 
    			+ filenameSeparator 
    			+ getVersion(context)
    			+ filenameSeparator
    			+ codenameDevice;
    	
    	return result;
    }
    
    public static File getFullFile(Context context){
    	return new File("storage/emulated/0/" + Environment.DIRECTORY_DOWNLOADS + "/" + RomUpdate.getFilename(context) + ".zip");
    }
}
