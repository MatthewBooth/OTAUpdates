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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ota.updates.R;
import com.ota.updates.RomUpdate;
import com.ota.updates.tasks.Changelog;
import com.ota.updates.tasks.LoadUpdateManifest;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Utils;

public class MainActivity extends Activity implements Constants{

	public final String TAG = this.getClass().getSimpleName();

	private static Context mContext;

	private Builder mCompatibilityDialog;
	private Builder mDonateDialog;
	private Builder mPlayStoreDialog;

	private boolean isLollipop;
	
	public static ProgressBar mProgressBar;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(MANIFEST_LOADED)) {
				// Reloads layouts to reflect the updated manifest information
				updateDonateLinkLayout();
				updateAddonsLayout();
				updateRomInformation();
				updateRomUpdateLayouts();
				updateWebsiteLayout();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mContext = this;
		setTheme(Preferences.getTheme(mContext));
		isLollipop = Utils.isLollipop();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ota_main);

		if (isLollipop) {
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
			setActionBar(toolbar);
			toolbar.setTitle(getResources().getString(R.string.app_name));
		} else {
			// Custom ActionBar view
			ActionBar actionBar = getActionBar();
			actionBar.setTitle(R.string.app_name);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,
					Gravity.END |
					Gravity.CENTER_VERTICAL);
			View actionbarView = LayoutInflater.from(this).inflate(R.layout.ota_main_actionbar_top, null);
			actionBar.setCustomView(actionbarView, layoutParams);
			actionBar.setDisplayShowCustomEnabled(true);
		}
		
		boolean firstRun = Preferences.getFirstRun(mContext);				
		if(firstRun) {
			Preferences.setFirstRun(mContext, false);
			showWhatsNew();
		}
		
		String oldChangelog = Preferences.getOldChangelog(mContext);
		String currentChangelog = getResources().getString(R.string.app_version);
		if(!oldChangelog.equals(currentChangelog)) {
			showWhatsNew();
		}
		
		// Create download directories if needed
		File installAfterFlashDir = new File(SD_CARD 
				+ File.separator
				+ OTA_DOWNLOAD_DIR
				+ File.separator
				+ INSTALL_AFTER_FLASH_DIR);
		installAfterFlashDir.mkdirs();

		createDialogs();

		// Check the correct build prop values are installed
		// Also executes the manifest/update check
		if (!Utils.isConnected(mContext)) {
			Builder notConnectedDialog = new Builder(mContext);
			notConnectedDialog.setTitle(R.string.main_not_connected_title)
			.setMessage(R.string.main_not_connected_message)
			.setPositiveButton(R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					((Activity) mContext).finish();
				}
			})
			.show();
		} else {
			new CompatibilityTask(mContext).execute();
		}

		// Has the download already completed?
		Utils.setHasFileDownloaded(mContext);

		// Update the layouts
		updateDonateLinkLayout();
		updateAddonsLayout();
		updateRomInformation();
		updateRomUpdateLayouts();
		updateWebsiteLayout();
	}

	@Override
	public void onStart() {
		super.onStart();
		this.registerReceiver(mReceiver, new IntentFilter(MANIFEST_LOADED));
	}

	@Override
	public void onStop() {
		super.onStop();
		this.unregisterReceiver(mReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (isLollipop)
			getMenuInflater().inflate(R.menu.ota_menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (isLollipop)
			switch (item.getItemId()) {
			case R.id.menu_changelog:
				openChangelog(null);
				return true;
			case R.id.menu_settings:
				openSettings(null);
				return true;
			}
			return false;
	}

	private void createDialogs() {
		// Compatibility Dialog
		mCompatibilityDialog = new AlertDialog.Builder(mContext);
		mCompatibilityDialog.setCancelable(false);
		mCompatibilityDialog.setTitle(R.string.main_not_compatible_title);
		mCompatibilityDialog.setMessage(R.string.main_not_compatible_message);
		mCompatibilityDialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
			}
		});
		
		// Donate Dialog
		mDonateDialog = new AlertDialog.Builder(this);
		String[] donateItems = { "PayPal", "BitCoin" };
		mDonateDialog.setTitle(getResources().getString(R.string.donate))		
		.setSingleChoiceItems(donateItems, 0, null)
		.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String url = "";
				int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
				if (selectedPosition == 0) {
					url = RomUpdate.getDonateLink(mContext);
				} else {
					url = RomUpdate.getBitCoinLink(mContext);
				}
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				
				try {
					startActivity(intent);
				} catch(ActivityNotFoundException ex) {
					// Nothing to handle BitCoin payments. Send to Play Store
					if (DEBUGGING)
						Log.d(TAG, ex.getMessage());					
					mPlayStoreDialog.show();
				}
			}
		})
		.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		mPlayStoreDialog = new AlertDialog.Builder(mContext);
		mPlayStoreDialog.setCancelable(true);
		mPlayStoreDialog.setTitle(R.string.main_playstore_title);
		mPlayStoreDialog.setMessage(R.string.main_playstore_message);
		mPlayStoreDialog.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String url = "https://play.google.com/store/search?q=bitcoin%20wallet&c=apps";
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});
		mPlayStoreDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
	}

	private void updateRomUpdateLayouts() {
		View updateAvailable;
		View updateNotAvailable;
		updateAvailable = (CardView) findViewById(R.id.layout_main_update_available);
		updateNotAvailable = (CardView) findViewById(R.id.layout_main_no_update_available);
		updateAvailable.setVisibility(View.GONE);
		updateNotAvailable.setVisibility(View.GONE);

		TextView updateAvailableSummary = (TextView) findViewById(R.id.main_tv_update_available_summary);
		TextView updateNotAvailableSummary = (TextView) findViewById(R.id.main_tv_no_update_available_summary);
		
		mProgressBar = (ProgressBar) findViewById(R.id.bar_main_progress_bar);
		mProgressBar.setVisibility(View.GONE);

		// Update is available
		if (RomUpdate.getUpdateAvailability(mContext) ||
                (!RomUpdate.getUpdateAvailability(mContext)) && Utils.isUpdateIgnored(mContext)) {
			updateAvailable.setVisibility(View.VISIBLE);
			TextView updateAvailableTitle = (TextView) findViewById(R.id.main_tv_update_available_title);

			if (Preferences.getDownloadFinished(mContext)) { //  Update already finished?
				updateAvailableTitle.setText(getResources().getString(R.string.main_update_finished));
				String htmlColorOpen = "";
				if (isLollipop) {
					if (Preferences.getCurrentTheme(mContext) == 0) { // Light
						htmlColorOpen = "<font color='#009688'>";
					} else {
						htmlColorOpen = "<font color='#80cbc4'>";
					}
				} else {
					htmlColorOpen = "<font color='#33b5e5'>";
				}
				String htmlColorClose = "</font>";
				String updateSummary = RomUpdate.getVersionName(mContext)
						+ "<br />"
						+ htmlColorOpen
						+ getResources().getString(R.string.main_download_completed_details)
						+ htmlColorClose;
				updateAvailableSummary.setText(Html.fromHtml(updateSummary));
			} else if (Preferences.getIsDownloadOnGoing(mContext)) {
				updateAvailableTitle.setText(getResources().getString(R.string.main_update_progress));
				mProgressBar.setVisibility(View.VISIBLE);
				String htmlColorOpen = "";
				if (isLollipop) {
					if (Preferences.getCurrentTheme(mContext) == 0) { // Light
						htmlColorOpen = "<font color='#009688'>";
					} else {
						htmlColorOpen = "<font color='#80cbc4'>";
					}
				} else {
					htmlColorOpen = "<font color='#33b5e5'>";
				}
				String htmlColorClose = "</font>";
				String updateSummary = htmlColorOpen
						+ getResources().getString(R.string.main_tap_to_view_progress)
						+ htmlColorClose;
				updateAvailableSummary.setText(Html.fromHtml(updateSummary));
			} else {
				updateAvailableTitle.setText(getResources().getString(R.string.main_update_available));
				String htmlColorOpen = "";
				if (isLollipop) {
					if (Preferences.getCurrentTheme(mContext) == 0) { // Light
						htmlColorOpen = "<font color='#009688'>";
					} else {
						htmlColorOpen = "<font color='#80cbc4'>";
					}
				} else {
					htmlColorOpen = "<font color='#33b5e5'>";
				}
				String htmlColorClose = "</font>";
				String updateSummary = RomUpdate.getVersionName(mContext)
						+ "<br />"
						+ htmlColorOpen
						+ getResources().getString(R.string.main_tap_to_download)
						+ htmlColorClose;
				updateAvailableSummary.setText(Html.fromHtml(updateSummary));

			}
		} else {
			updateNotAvailable.setVisibility(View.VISIBLE);

			boolean is24 = DateFormat.is24HourFormat(mContext);
			Date now = new Date();
			Locale locale = Locale.getDefault();
			String time = "";

			if (is24) {
				time = new SimpleDateFormat("d, MMMM HH:mm", locale).format(now);
			} else {
				time = new SimpleDateFormat("d, MMMM hh:mm a", locale).format(now);
			}

			Preferences.setUpdateLastChecked(this, time);
			String lastChecked = getString(R.string.main_last_checked);
			updateNotAvailableSummary.setText(lastChecked + " " + time);
		}
	}
	
	private void updateAddonsLayout() {
		CardView addonsLink = (CardView) findViewById(R.id.layout_main_addons);
		addonsLink.setVisibility(View.GONE);
		
		if (RomUpdate.getAddonsCount(mContext) > 0) {
			addonsLink.setVisibility(View.VISIBLE);
		}
	}

	private void updateDonateLinkLayout() {
		CardView donateLink = (CardView) findViewById(R.id.layout_main_dev_donate_link);
		donateLink.setVisibility(View.GONE);
		
		if (!(RomUpdate.getDonateLink(mContext).trim().equals("null")) 
				|| !(RomUpdate.getBitCoinLink(mContext).trim().equals("null"))) {
			donateLink.setVisibility(View.VISIBLE);
		}
	}

	private void updateWebsiteLayout() {
		CardView webLink = (CardView) findViewById(R.id.layout_main_dev_website);
		webLink.setVisibility(View.GONE);

		if (!RomUpdate.getWebsite(mContext).trim().equals("null")) {
			webLink.setVisibility(View.VISIBLE);
		}
	}

	private void updateRomInformation() {
		String htmlColorOpen = "";
		if (isLollipop) {
			if (Preferences.getCurrentTheme(mContext) == 0) { // Light
				htmlColorOpen = "<font color='#009688'>";
			} else {
				htmlColorOpen = "<font color='#80cbc4'>";
			}
		} else {
			htmlColorOpen = "<font color='#33b5e5'>";
		}
		String htmlColorClose = "</font>";

		//ROM name
		TextView romName = (TextView) findViewById(R.id.tv_main_rom_name);
		String romNameTitle = getApplicationContext().getResources().getString(R.string.main_rom_name) + " ";
		String romNameActual = Utils.getProp(OTA_ROMNAME);
		romName.setText(Html.fromHtml(romNameTitle + htmlColorOpen + romNameActual + htmlColorClose));

		//ROM version
		TextView romVersion = (TextView) findViewById(R.id.tv_main_rom_version);
		String romVersionTitle = getApplicationContext().getResources().getString(R.string.main_rom_version) + " ";
		String romVersionActual = Utils.getProp(OTA_VERSION);
		romVersion.setText(Html.fromHtml(romVersionTitle + htmlColorOpen + romVersionActual + htmlColorClose));

		//ROM date
		TextView romDate = (TextView) findViewById(R.id.tv_main_rom_date);
		String romDateTitle = getApplicationContext().getResources().getString(R.string.main_rom_build_date) + " ";
		String romDateActual = Utils.getProp("ro.build.date");
		romDate.setText(Html.fromHtml(romDateTitle + htmlColorOpen + romDateActual + htmlColorClose));

		//ROM android version
		TextView romAndroid = (TextView) findViewById(R.id.tv_main_android_version);
		String romAndroidTitle = getApplicationContext().getResources().getString(R.string.main_android_verison) + " ";
		String romAndroidActual = Utils.getProp("ro.build.version.release");
		romAndroid.setText(Html.fromHtml(romAndroidTitle + htmlColorOpen + romAndroidActual + htmlColorClose));

		//ROM developer
		TextView romDeveloper = (TextView) findViewById(R.id.tv_main_rom_developer);
		boolean showDevName = !RomUpdate.getDeveloper(this).equals("null");
		romDeveloper.setVisibility(showDevName? View.VISIBLE : View.GONE);

		String romDeveloperTitle = getApplicationContext().getResources().getString(R.string.main_rom_developer) + " ";
		String romDeveloperActual = RomUpdate.getDeveloper(this);
		romDeveloper.setText(Html.fromHtml(romDeveloperTitle + htmlColorOpen + romDeveloperActual + htmlColorClose));

	}

	public void openCheckForUpdates(View v) {
		new LoadUpdateManifest(mContext, true).execute();
	}

	public void openDownload(View v) {
		Intent intent = new Intent(mContext, AvailableActivity.class);
		startActivity(intent);
	}
	
	public void openAddons(View v) {
		Intent intent = new Intent(mContext, AddonActivity.class);
		startActivity(intent);
	}

	public void openDonationPage(View v) {
		
		boolean payPalLinkAvailable = RomUpdate.getDonateLink(mContext).trim().equals("null");
		boolean bitCoinLinkAvailable = RomUpdate.getBitCoinLink(mContext).trim().equals("null");
		if (!payPalLinkAvailable && !bitCoinLinkAvailable) {
			mDonateDialog.show();
		} else if (!payPalLinkAvailable && bitCoinLinkAvailable) {
			String url = RomUpdate.getDonateLink(mContext);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);			
		} else if (payPalLinkAvailable && !bitCoinLinkAvailable) {
			String url = RomUpdate.getBitCoinLink(mContext);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);			
		} else {
			// Shouldn't be here
		}
	}

	public void openWebsitePage(View v) {
		String url = RomUpdate.getWebsite(mContext);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	public void openSettings(View v) {
		Intent intent = new Intent(mContext, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void openChangelog (View v) {
		String title = getResources().getString(R.string.changelog);
		String changelog = RomUpdate.getChangelog(mContext);
		new Changelog(this, mContext, title, changelog, false).execute();
	}

	private void showWhatsNew() {
		String title = getResources().getString(R.string.changelog);
		String changelog = getResources().getString(R.string.changelog_url);
		new Changelog(this, mContext, title, changelog, true).execute();
	}
	
	public static void updateProgress(int progress, int downloaded, int total, Activity activity) {
		if(mProgressBar != null) {
			mProgressBar.setProgress((int) progress);
		}
	}

	public class CompatibilityTask extends AsyncTask<Void, Boolean, Boolean> implements Constants{

		public final String TAG = this.getClass().getSimpleName();

		private Context mContext;
		private String mPropName;

		public CompatibilityTask(Context context) {
			mContext = context;
			mPropName = mContext.getResources().getString(R.string.prop_name);
		}

		@Override
		protected Boolean doInBackground(Void... v) {
			return Utils.doesPropExist(mPropName);
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {
				if (DEBUGGING)
					Log.d(TAG, "Prop found");
				new LoadUpdateManifest(mContext, true).execute();
			} else {
				if (DEBUGGING)
					Log.d(TAG, "Prop not found");
				try {
					mCompatibilityDialog.show();
				} catch(WindowManager.BadTokenException ex) {
					Log.e(TAG, ex.getMessage());
				}
			}
			super.onPostExecute(result);
		}
	}
}