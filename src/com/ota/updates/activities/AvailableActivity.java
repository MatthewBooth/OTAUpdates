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

import in.uncod.android.bypass.Bypass;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ota.updates.R;
import com.ota.updates.RomUpdate;
import com.ota.updates.download.DownloadRom;
import com.ota.updates.download.DownloadRomProgress;
import com.ota.updates.tasks.GenerateRecoveryScript;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Tools;
import com.ota.updates.utils.Utils;

public class AvailableActivity extends Activity implements Constants, android.view.View.OnClickListener {

	private static Context mContext;
	public final static String TAG = "AvailableActivity";

	public static ProgressBar mProgressBar;
	public static TextView mProgressCounterText;

	private Builder mDeleteDialog;
	private Builder mRebootDialog;
	private Builder mRebootManualDialog;
	private Builder mNetworkDialog;

	private static Button mCheckMD5Button;
	private static Button mDeleteButton;
	private static Button mInstallButton;
	private static Button mDownloadButton;
	private static Button mCancelButton;

	private DownloadRom mDownloadRom;


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		setTheme(Preferences.getTheme(mContext));
		super.onCreate(savedInstanceState);          
		setContentView(R.layout.ota_available);

		if (Utils.isLollipop()) {
			Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_available_bottom);
			toolbarBottom.setTitle("");
		}

		mDownloadRom = new DownloadRom();

		mProgressBar = (ProgressBar) findViewById(R.id.bar_available_progress_bar);
		mProgressCounterText = (TextView) findViewById(R.id.tv_available_progress_counter);

		if (Utils.isLollipop()) {
			mCheckMD5Button = (Button) findViewById(R.id.menu_available_check_md5);
			mDeleteButton = (Button) findViewById(R.id.menu_available_delete);
			mInstallButton = (Button) findViewById(R.id.menu_available_install);
			mDownloadButton = (Button) findViewById(R.id.menu_available_download);
			mCancelButton = (Button) findViewById(R.id.menu_available_cancel);

			mCheckMD5Button.setOnClickListener(this);
			mDeleteButton.setOnClickListener(this);
			mInstallButton.setOnClickListener(this);
			mDownloadButton.setOnClickListener(this);
			mCancelButton.setOnClickListener(this);
		}

		setupDialogs();
		setupUpdateNameInfo();
		setupProgress(mContext);
		setupMd5Info();
		setupRomHut();
		setupChangeLog();
		if (Utils.isLollipop()) {
			setupMenuToolbar(mContext);
		}

		if (Preferences.getIsDownloadOnGoing(mContext)) {
			// If the activity has already been run, and the download started 
			// Then start updating the progress bar again
			if (DEBUGGING)
				Log.d(TAG, "Starting progress updater");
			DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			new DownloadRomProgress(mContext, downloadManager).execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ota_menu_available, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_available_check_md5:
			new MD5Check(mContext).execute();
			Preferences.setHasMD5Run(mContext, true);
			return true;
		case R.id.menu_available_delete:
			mDeleteDialog.show();
			return true;
		case R.id.menu_available_download:
			download();
			return true;
		case R.id.menu_available_cancel:
			mDownloadRom.cancelDownload(mContext);
			setupUpdateNameInfo();
			setupProgress(mContext);
			invalidateOptionsMenu();
			return true;
		case R.id.menu_available_install:
			if (!Tools.isRootAvailable()) {
				mRebootManualDialog.show();
			} else {
				mRebootDialog.show();
			}    
			return true;
		default:
			return super.onOptionsItemSelected(item);           
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem downloadMenuItem = menu.findItem(R.id.menu_available_download);
		MenuItem cancelMenuItem = menu.findItem(R.id.menu_available_cancel);
		MenuItem installMenuItem = menu.findItem(R.id.menu_available_install);
		MenuItem md5MenuItem = menu.findItem(R.id.menu_available_check_md5);
		MenuItem deleteMenuItem = menu.findItem(R.id.menu_available_delete);

		boolean downloadFinished = Preferences.getDownloadFinished(mContext);
		boolean downloadIsRunning = Preferences.getIsDownloadOnGoing(mContext);
		boolean md5HasRun = Preferences.getHasMD5Run(mContext);
		boolean md5Passed = Preferences.getMD5Passed(mContext);

		if (!downloadFinished) { // Download hasn't finished
			if (downloadIsRunning) { 
				// Download is still running
				downloadMenuItem.setVisible(false);
				cancelMenuItem.setVisible(true);
				installMenuItem.setVisible(false);
			} else { 
				// Download is not running and hasn't finished
				downloadMenuItem.setVisible(true);
				cancelMenuItem.setVisible(false);
				installMenuItem.setVisible(false);
			}
		} else { // Download has finished
			String md5 = RomUpdate.getMd5(mContext);
			if (!md5.equals("null")) { 
				// Is MD5 being used?
				if (md5HasRun && md5Passed) {
					md5MenuItem.setEnabled(false);
					md5MenuItem.setTitle(R.string.available_md5_ok);
				} else if (md5HasRun && !md5Passed) {
					md5MenuItem.setEnabled(false);
					md5MenuItem.setTitle(R.string.available_md5_failed);
				} else  if (!md5HasRun) {
					md5MenuItem.setEnabled(true);
				}
			} else {
				md5MenuItem.setEnabled(false);
			}
			deleteMenuItem.setEnabled(true);
			downloadMenuItem.setVisible(false);
			cancelMenuItem.setVisible(false);
			installMenuItem.setVisible(true);
		}
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.menu_available_check_md5:
			new MD5Check(mContext).execute();
			Preferences.setHasMD5Run(mContext, true);
			break;
		case R.id.menu_available_delete:
			mDeleteDialog.show();
			break;
		case R.id.menu_available_install:
			if (!Tools.isRootAvailable()) {
				mRebootManualDialog.show();
			} else {
				mRebootDialog.show();
			}		
			break;
		case R.id.menu_available_download:
			download();
			break;
		case R.id.menu_available_cancel:
			mDownloadRom.cancelDownload(mContext);
			setupUpdateNameInfo();
			setupProgress(mContext);
			setupMenuToolbar(mContext);
			break;
		}
	}

	private void setupDialogs() {
		mDeleteDialog = new AlertDialog.Builder(mContext);
		mDeleteDialog.setTitle(R.string.are_you_sure)
		.setMessage(R.string.available_delete_confirm_message)
		.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Proceed to delete the file, and reset most variables
				// and layouts

				Utils.deleteFile(RomUpdate.getFullFile(mContext)); // Delete the file
				Preferences.setHasMD5Run(mContext, false); // MD5 check hasn't been run
				Preferences.setDownloadFinished(mContext, false);
				setupUpdateNameInfo(); // Update name info
				setupProgress(mContext); // Progress goes back to 0
				setupMd5Info(); // MD5 goes back to default
				if (Utils.isLollipop()) {
					setupMenuToolbar(mContext); // Reset options menu
				} else {
					invalidateMenu();
				}
			}
		}).setNegativeButton(R.string.cancel, null);

		mRebootDialog = new AlertDialog.Builder(mContext);
		mRebootDialog.setTitle(R.string.are_you_sure)
		.setMessage(R.string.available_reboot_confirm)
		.setPositiveButton(R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (DEBUGGING)
					Log.d(TAG,
							"ORS is "
									+ Preferences
									.getORSEnabled(mContext));

				if (Preferences.getORSEnabled(mContext)) {
					new GenerateRecoveryScript(mContext).execute();
				} else {
					Tools.recovery(mContext);
				}
			}
		}).setNegativeButton(R.string.cancel, null);

		mNetworkDialog = new Builder(mContext);
		mNetworkDialog.setTitle(R.string.available_wrong_network_title)
		.setMessage(R.string.available_wrong_network_message)
		.setPositiveButton(R.string.ok, null)
		.setNeutralButton(R.string.settings, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(mContext, SettingsActivity.class);
				mContext.startActivity(intent);
			}
		});

		mRebootManualDialog = new AlertDialog.Builder(mContext);
		mRebootManualDialog.setTitle(R.string.available_reboot_manual_title)
		.setMessage(R.string.available_reboot_manual_message)
		.setPositiveButton(R.string.cancel, null);
	}

	public static void setupMenuToolbar(Context context) {
		boolean downloadFinished = Preferences.getDownloadFinished(context);
		boolean downloadIsRunning = Preferences.getIsDownloadOnGoing(context);
		boolean md5HasRun = Preferences.getHasMD5Run(context);
		boolean md5Passed = Preferences.getMD5Passed(context);

		mDeleteButton.setEnabled(false);
		mCheckMD5Button.setEnabled(false);

		if (!downloadFinished) { // Download hasn't finished
			if (downloadIsRunning) {
				// Download is still running
				mDownloadButton.setVisibility(View.GONE);
				mCancelButton.setVisibility(View.VISIBLE);
				mInstallButton.setVisibility(View.GONE);
			} else {
				// Download is not running and hasn't finished
				mDownloadButton.setVisibility(View.VISIBLE);
				mCancelButton.setVisibility(View.GONE);
				mInstallButton.setVisibility(View.GONE);
			}
		} else { // Download has finished
			String md5 = RomUpdate.getMd5(context);
			if (!md5.equals("null")) {
				// Is MD5 being used?
				if (md5HasRun && md5Passed) {
					mCheckMD5Button.setEnabled(false);
					mCheckMD5Button.setText(R.string.available_md5_ok);
				} else if (md5HasRun && !md5Passed) {
					mCheckMD5Button.setEnabled(false);
					mCheckMD5Button.setText(R.string.available_md5_failed);
				} else if (!md5HasRun) {
					mCheckMD5Button.setEnabled(true);
				}
			} else {
				mCheckMD5Button.setClickable(false);
			}
			mDeleteButton.setEnabled(true);
			mDownloadButton.setVisibility(View.GONE);
			mCancelButton.setVisibility(View.GONE);
			mInstallButton.setVisibility(View.VISIBLE);
		}
	}

	private void setupChangeLog() {
		TextView changelogView = (TextView) findViewById(R.id.tv_available_changelog_content);
		Bypass byPass = new Bypass(this);
		String changeLogStr = RomUpdate.getChangelog(mContext);
		CharSequence string = byPass.markdownToSpannable(changeLogStr);
		changelogView.setText(string);
		changelogView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void setupRomHut() {
		String domainText = RomUpdate.getUrlDomain(mContext);
		boolean isRomHut = domainText.contains("romhut.com");
		if (domainText != null) {
			TextView domainTextView = (TextView) findViewById(R.id.tv_available_romhut);
			String sponsoredBy = isRomHut ?  "Sponsored by " : "";
			domainTextView.setText(sponsoredBy + domainText);
			if (Utils.isLollipop()) {	
				int color;
				if (Preferences.getCurrentTheme(mContext) == 0) { // Light
					color = getResources().getColor(R.color.material_deep_teal_500);
				} else {
					color = getResources().getColor(R.color.material_deep_teal_200);
				}
				domainTextView.setTextColor(color);
			} else {
				domainTextView.setTextColor(getResources().getColor(R.color.holo_blue_light));
			}
		}
	}

	private void setupUpdateNameInfo() {
		boolean isDownloadOnGoing = Preferences.getIsDownloadOnGoing(mContext);
		TextView updateNameInfoText = (TextView) findViewById(R.id.tv_available_update_name);
		String downloading = getResources().getString(R.string.available_downloading);
		String filename = RomUpdate.getVersionName(mContext);

		if (Utils.isLollipop()) {
			int color;
			if (Preferences.getCurrentTheme(mContext) == 0) { // Light
				color = getResources().getColor(R.color.material_deep_teal_500);
			} else {
				color = getResources().getColor(R.color.material_deep_teal_200);
			}
			updateNameInfoText.setTextColor(color);
		} else {
			updateNameInfoText.setTextColor(getResources().getColor(R.color.holo_blue_light));
		}

		if (isDownloadOnGoing) {
			updateNameInfoText.setText(downloading); 	
		} else {			
			updateNameInfoText.setText(filename); 	
		}
	}

	private void setupMd5Info() {
		TextView md5Text = (TextView) findViewById(R.id.tv_available_md5);
		String md5Prefix = getResources().getString(R.string.available_md5);
		String md5 = RomUpdate.getMd5(mContext);
		if (md5.equals("null")) {
			md5Text.setText(md5Prefix + " N/A"); 
		} else {
			md5Text.setText(md5Prefix + " " + md5);
		}  	
	}

	private void download() {
		String httpUrl = RomUpdate.getHttpUrl(mContext);
		String directUrl = RomUpdate.getDirectUrl(mContext);
		String error = getResources().getString(R.string.available_url_error);
		
		if(DEBUGGING)
			Log.d(TAG, "HTTPURL = " + httpUrl + " DirectURL = " + directUrl);

		boolean isMobile = Utils.isMobileNetwork(mContext);
		boolean isSettingWiFiOnly = Preferences.getNetworkType(mContext).equals(WIFI_ONLY);

		if (isMobile && isSettingWiFiOnly) {
			mNetworkDialog.show();
		} else {
			// We're good, open links or start downloads
			boolean directUrlEmpty = directUrl.equals("null") || directUrl.isEmpty();
			boolean httpUrlEmpty = httpUrl.equals("null") || httpUrl.isEmpty();
			
			if (directUrlEmpty) {
				if (DEBUGGING)
					Log.d(TAG, "HTTP link opening");
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(httpUrl));
				startActivity(intent);
			} else if (httpUrlEmpty || !directUrlEmpty) {
				if (DEBUGGING)
					Log.d(TAG, "Downloading via DownloadManager");
				mDownloadRom.startDownload(mContext);
				setupUpdateNameInfo();
				if (Utils.isLollipop()) {
					setupMenuToolbar(mContext); // Reset options menu
				} else {
					invalidateMenu();
				}
			} else {
				if (DEBUGGING)
					Log.e(TAG, "No links found");
				Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
			}
		}
	}

	public static void setupProgress(Context context) {
		Resources res = context.getResources();
		if (DEBUGGING)
			Log.d(TAG, "Setting up Progress Bars");
		boolean downloadFinished = Preferences.getDownloadFinished(context);
		if (downloadFinished) {
			if (DEBUGGING)
				Log.d(TAG, "Download finished. Setting up Progress Bars accordingly.");
			String ready = context.getResources().getString(R.string.available_ready_to_install);

			int color = res.getColor(R.color.holo_blue_light);
			if (Utils.isLollipop()) {
				if (Preferences.getCurrentTheme(context) == 0) { // Light
					color = context.getResources().getColor(R.color.material_deep_teal_500);
				} else {
					color = context.getResources().getColor(R.color.material_deep_teal_200);
				}		
			} 
			if(mProgressCounterText != null) {
				mProgressCounterText.setTextColor(color);
				mProgressCounterText.setText(ready);
			}
			if(mProgressBar != null) {
				mProgressBar.setProgress(100);
			}
		} else {
			if (DEBUGGING)
				Log.d(TAG, "Download not finished/started. Setting Progress Bars to default.");
			int fileSize = RomUpdate.getFileSize(context);
			String fileSizeStr = Utils.formatDataFromBytes(fileSize);
			if(mProgressCounterText != null) {
				mProgressCounterText.setText(fileSizeStr);
			}
			if(mProgressBar != null) {
				mProgressBar.setProgress(0);
			}
		}
	}

	public static void updateProgress(int progress, int downloaded, int total, Context context) {
		mProgressBar.setProgress((int) progress);
		mProgressCounterText.setText(
				Utils.formatDataFromBytes(downloaded) + 
				"/" + 
				Utils.formatDataFromBytes(total));
	}

	public static void invalidateMenu() {
		((Activity) mContext).invalidateOptionsMenu();
	}

	public class MD5Check extends AsyncTask<Object, Boolean, Boolean>{

		public final String TAG = this.getClass().getSimpleName();

		Context mContext;
		ProgressDialog mMD5CheckDialog;

		public MD5Check(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			// Setup Checking dialog
			mMD5CheckDialog = new ProgressDialog(mContext);
			mMD5CheckDialog.setCancelable(false);
			mMD5CheckDialog.setIndeterminate(true);
			mMD5CheckDialog.setMessage(mContext.getString(R.string.available_checking_md5));
			mMD5CheckDialog.show();
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			String file = RomUpdate.getFullFile(mContext).getAbsolutePath(); // Full file, with path
			String md5Remote = RomUpdate.getMd5(mContext); // Remote MD5 form the manifest. This is what we expect it to be
			String md5Local = Tools.shell("md5sum " + file + " | cut -d ' ' -f 1", false); // Run the check on our local file
			md5Local = md5Local.trim(); // Trim both to remove any whitespace
			md5Remote = md5Remote.trim();			
			return md5Local.equalsIgnoreCase(md5Remote); // Return the comparison
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mMD5CheckDialog.cancel(); // Remove dialog

			// Show toast letting the user know immediately
			if (result) {
				Toast.makeText(mContext, mContext.getString(R.string.available_md5_ok), Toast.LENGTH_LONG).show();           
			} else {
				Toast.makeText(mContext, mContext.getString(R.string.available_md5_failed), Toast.LENGTH_LONG).show();           
			}

			Preferences.setMD5Passed(mContext, result); // Set value for other persistent settings
			if (Utils.isLollipop()) {
				setupMenuToolbar(mContext); // Reset options menu
			} else {
				invalidateMenu();
			}
			super.onPostExecute(result);
		}
	}
}
