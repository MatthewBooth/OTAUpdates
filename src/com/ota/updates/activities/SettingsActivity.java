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

package com.ota.updates.activities;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;

import com.ota.updates.R;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Tools;
import com.ota.updates.utils.Utils;

@SuppressLint("SdCardPath")
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener, OnSharedPreferenceChangeListener, Constants{

	public final String TAG = this.getClass().getSimpleName();
	private static final String NOTIFICATIONS_IGNORED_RELEASE = "notifications_ignored_release";

	private Context mContext;
	private Builder mInstallPrefsDialog;
	private Preference mInstallPrefs;
	private Preference mAboutActivity;
	private RingtonePreference mRingtonePreference;
	private SparseBooleanArray mInstallPrefsItems = new SparseBooleanArray();
	private SwitchPreference mIgnoredRelease;
	private ListPreference mThemePref;
	private Preference mProPreference;
	private Preference mStorageLocation;

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = this;
		setTheme(Preferences.getSettingsTheme(mContext));
		super.onCreate(savedInstanceState);

		getPreferenceManager().setSharedPreferencesName(Preferences.PREF_NAME);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		addPreferencesFromResource(R.xml.preferences);

		mInstallPrefs = (Preference) findPreference(INSTALL_PREFS);
		mInstallPrefs.setOnPreferenceClickListener(this);

		mAboutActivity = (Preference) findPreference(ABOUT_ACTIVITY_PREF);
		mAboutActivity.setOnPreferenceClickListener(this);

		mRingtonePreference = (RingtonePreference) findPreference(NOTIFICATIONS_SOUND);

		mThemePref = (ListPreference) findPreference(CURRENT_THEME);
		mThemePref.setValue(Integer.toString(Preferences.getCurrentTheme(mContext)));
		setThemeSummary();

		String defValue = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString();
		String soundValue = getPreferenceManager().getSharedPreferences().getString(NOTIFICATIONS_SOUND, defValue);
		setRingtoneSummary(soundValue);

		if (!Tools.isRootAvailable()) {
			SwitchPreference ors = (SwitchPreference) findPreference("updater_twrp_ors");
			ors.setEnabled(false);
		}

		mIgnoredRelease = (SwitchPreference) findPreference(NOTIFICATIONS_IGNORED_RELEASE);
		mIgnoredRelease.setOnPreferenceChangeListener(this);
		String ignoredRelease = Preferences.getIgnoredRelease(mContext);
		boolean isIgnored = ignoredRelease.equalsIgnoreCase("0");
		if (!isIgnored) {
			mIgnoredRelease.setSummary(
					getResources().getString(R.string.notification_ignoring_release) +
					" " + 
					ignoredRelease);
			mIgnoredRelease.setChecked(true);
			mIgnoredRelease.setEnabled(true);
			mIgnoredRelease.setSelectable(true);
		} else {
			setNotIgnore(false);
		}

		mProPreference = (Preference) findPreference(ABOUT_PREF_PRO);
		mProPreference.setOnPreferenceClickListener(this);

		Boolean isPro = Utils.isPackageInstalled("com.ota.updatespro", mContext);
		if (isPro) {		
			mProPreference.setLayoutResource(R.layout.preference_pro);
			mProPreference.setTitle(R.string.about_pro_title);
			mProPreference.setSummary(R.string.about_pro_summary);
			mProPreference.setSelectable(!isPro);
		} else {
			mProPreference.setLayoutResource(R.layout.preference_no_pro);
			mProPreference.setTitle(R.string.about_pro_title);
			mProPreference.setSummary(R.string.about_non_pro_summary);
		}
		Preferences.setIsPro(mContext, isPro);
		
		mStorageLocation = (Preference) findPreference(STORAGE_LOCATION);
		mStorageLocation.setSelectable(false);
		String storageLocationStr = SD_CARD + File.separator + OTA_DOWNLOAD_DIR;
		mStorageLocation.setSummary(storageLocationStr);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		mRingtonePreference.setOnPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference pref = findPreference(key);
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());

			if (key.equals(CURRENT_THEME)) {
				Preferences.setTheme(mContext, listPref.getValue());
				Intent intent = new Intent(mContext, MainActivity.class);
				startActivity(intent);
			} else if (key.equals(UPDATER_BACK_FREQ)) {
				Utils.setBackgroundCheck(mContext, Preferences.getBackgroundService(mContext));
			}
		} else if (pref instanceof SwitchPreference) {
			if (key.equals(UPDATER_BACK_SERVICE)) {
				Utils.setBackgroundCheck(mContext, Preferences.getBackgroundService(mContext));
			}
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String otaPackage = "com.ota.updatespro";

		if (preference == mInstallPrefs) {
			showInstallPrefs();
		} else if (preference == mAboutActivity) {
			Intent intent = new Intent(mContext, AboutActivity.class);
			startActivity(intent);
		} else if (preference == mProPreference) {
			String url = "https://play.google.com/store/apps/details?id=" + otaPackage;
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
		}
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean result = false;
		if (preference == mRingtonePreference) {
			setRingtoneSummary((String)newValue);
			result = true;
		} else if (preference == mIgnoredRelease) {
			if (!(Boolean) newValue) {
				if (DEBUGGING) {
					Log.d(TAG, "Unignoring release");
				}
				setNotIgnore(true);
			}
		}
		return result;
	}

	private void setNotIgnore(boolean set) {
		if (set) {
			Preferences.setIgnoredRelease(mContext, "0");
		}
		mIgnoredRelease.setSummary(
				getResources().getString(R.string.notification_not_ignoring_release));
		mIgnoredRelease.setChecked(false);
		mIgnoredRelease.setEnabled(false);
		mIgnoredRelease.setSelectable(false);
	}

	private void showInstallPrefs() {
		boolean wipeData, wipeCache, wipeDalvik, deleteAfterInstall;

		wipeData = Preferences.getWipeData(mContext);
		wipeCache = Preferences.getWipeCache(mContext);
		wipeDalvik = Preferences.getWipeDalvik(mContext);
		deleteAfterInstall = Preferences.getDeleteAfterInstall(mContext);

		// Default value array for the multichoice class.
		boolean [] defaultValues = { wipeData, wipeCache, wipeDalvik, deleteAfterInstall };

		// Also fill in InstallPrefItems with the default values
		// So that, if the user changes nothing, it doesn't reset all to false.
		mInstallPrefsItems.put(0, wipeData);
		mInstallPrefsItems.put(1, wipeCache);
		mInstallPrefsItems.put(2, wipeDalvik);
		mInstallPrefsItems.put(3, deleteAfterInstall);

		mInstallPrefsDialog = new AlertDialog.Builder(mContext);
		mInstallPrefsDialog.setTitle(R.string.twrp_ors_install_prefs);
		mInstallPrefsDialog.setMultiChoiceItems(R.array.ors_install_entries, defaultValues,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {            
				mInstallPrefsItems.put(which, isChecked);
			}
		});
		mInstallPrefsDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Preferences.setWipeData(mContext, mInstallPrefsItems.get(0));
				Preferences.setWipeCache(mContext, mInstallPrefsItems.get(1));   
				Preferences.setWipeDalvik(mContext, mInstallPrefsItems.get(2));   
				Preferences.setDeleteAfterInstall(mContext, mInstallPrefsItems.get(3));   
			}
		});
		mInstallPrefsDialog.show();
	}

	private void setRingtoneSummary(String soundValue) {
		Uri soundUri = TextUtils.isEmpty(soundValue) ? null : Uri.parse(soundValue);
		Ringtone tone = soundUri != null ? RingtoneManager.getRingtone(this, soundUri) : null;
		mRingtonePreference.setSummary(tone != null ? tone.getTitle(this)
				: getResources().getString(R.string.silent_ringtone));
	}

	private void setThemeSummary() {	
		int currentTheme = Preferences.getCurrentTheme(mContext);
		if(DEBUGGING)
			Log.d(TAG, "Current theme number is" + currentTheme);
		int id = 0;
		for (int i = 0; i < mThemePref.getEntryValues().length; i++) {
			if (mThemePref.getEntryValues()[i].equals(Integer.toString(currentTheme))) {
				id = i;
				break;
			}
		}
		mThemePref.setSummary(mThemePref.getEntries()[id]);
	}
}