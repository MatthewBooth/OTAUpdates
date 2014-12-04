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

package com.ota.updates.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.ota.updates.DownloadRomUpdate;
import com.ota.updates.R;
import com.ota.updates.RomUpdate;
import com.ota.updates.tasks.GenerateRecoveryScript;
import com.ota.updates.tasks.UpdateProgress;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Tools;
import com.ota.updates.utils.Utils;

public class AvailableActivity extends Activity implements Constants {

	private static Context mContext;
	public final static String TAG = "AvailableActivity";

	public static ProgressBar mProgressBar;
	public static TextView mProgressCounterText;

	private Builder mDeleteDialog;
	private Builder mRebootDialog;
	private Builder mNetworkDialog;


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		setTheme(Preferences.getTheme(mContext));
		super.onCreate(savedInstanceState);          
		setContentView(R.layout.ota_available);
		
		if(Utils.isLollipop()){
			Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_available_bottom);
			toolbarBottom.setTitle("");
			setActionBar(toolbarBottom);
		}

		mProgressBar = (ProgressBar) findViewById(R.id.bar_available_progress_bar);
		mProgressCounterText = (TextView) findViewById(R.id.tv_available_progress_counter);

		setupUpdateNameInfo();
		setupProgress();
		setupMd5Info();
		setupChangeLog();

		if(Preferences.getIsDownloadOnGoing(mContext)){
			// If the activity has already been run, and the download started 
			// Then start updating the progress bar again
			if(DEBUGGING)
				Log.d(TAG, "Starting progress updater");
			DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			new UpdateProgress(mContext, downloadManager).execute();
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
			mDeleteDialog = new AlertDialog.Builder(mContext);
			mDeleteDialog.setIconAttribute(R.attr.alertIcon)
			.setTitle(R.string.are_you_sure)
			.setMessage(R.string.available_delete_confirm_message)
			.setPositiveButton(R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Proceed to delete the file, and reset most variables and layouts

					Utils.deleteFile(RomUpdate.getFullFile(mContext)); // Delete the file
					Preferences.setHasMD5Run(mContext, false); // MD5 check hasn't been run
					Preferences.setDownloadFinished(mContext, false);
					setupUpdateNameInfo(); // Update name info
					setupProgress(); // Progress goes back to 0
					setupMd5Info(); // MD5 goes back to default
					invalidateOptionsMenu(); // Reset options menu				
				}
			})
			.setNegativeButton(R.string.cancel, null);
			mDeleteDialog.show();
			return true;
		case R.id.menu_available_download:
			download();
			return true;
		case R.id.menu_available_cancel:
			DownloadRomUpdate.cancelDownload(mContext);
			setupUpdateNameInfo();
			setupProgress();
			invalidateOptionsMenu();
			return true;
		case R.id.menu_available_install:
			mRebootDialog = new AlertDialog.Builder(mContext);
			mRebootDialog.setIconAttribute(R.attr.alertIcon)
			.setTitle(R.string.are_you_sure)
			.setMessage(R.string.available_reboot_confirm)
			.setPositiveButton(R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(DEBUGGING)
						Log.d(TAG, "ORS is " + Preferences.getORSEnabled(mContext));
					
					if(Preferences.getORSEnabled(mContext)){
						new GenerateRecoveryScript(mContext).execute();
					} else {
						Tools.recovery();
					}
				}
			})
			.setNegativeButton(R.string.cancel, null);
			mRebootDialog.show();     
			return true;
		default:
			return super.onOptionsItemSelected(item);           
		}
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

		if(!downloadFinished){ // Download hasn't finished
			if(downloadIsRunning){ 
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
			if(!md5.equals("null")){ 
				// Is MD5 being used?
				if(md5HasRun && md5Passed){
					md5MenuItem.setEnabled(false);
					md5MenuItem.setTitle(R.string.available_md5_ok);
				} else if (md5HasRun && !md5Passed){
					md5MenuItem.setEnabled(false);
					md5MenuItem.setTitle(R.string.available_md5_failed);
				} else  if(!md5HasRun){
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

	private void setupChangeLog(){
		TextView changelogView = (TextView) findViewById(R.id.tv_available_changelog_content);
		String[] changeLogStr = RomUpdate.getChangelog(mContext).split(";");
		changelogView.setText(Utils.getBulletList(
				RomUpdate.getCodename(mContext) + " " + RomUpdate.getVersion(mContext), changeLogStr));
	}

	private void setupUpdateNameInfo(){
		boolean isDownloadOnGoing = Preferences.getIsDownloadOnGoing(mContext);
		TextView updateNameInfoText = (TextView) findViewById(R.id.tv_available_update_name);
		String downloading = getResources().getString(R.string.available_downloading);
		String filename = RomUpdate.getFilename(mContext);

		if(Utils.isLollipop()){
			updateNameInfoText.setTextColor(getResources().getColor(R.color.material_teal_500));
		} else {
			updateNameInfoText.setTextColor(getResources().getColor(R.color.holo_blue_light));
		}
		
		if(isDownloadOnGoing){
			updateNameInfoText.setText(downloading); 	
		} else {			
			updateNameInfoText.setText(filename); 	
		}
	}

	private void setupMd5Info(){
		TextView md5Text = (TextView) findViewById(R.id.tv_available_md5);
		String md5Prefix = getResources().getString(R.string.available_md5);
		String md5 = RomUpdate.getMd5(mContext);
		if(md5.equals("null")){
			md5Text.setText(md5Prefix + " N/A"); 
		} else {
			md5Text.setText(md5Prefix + " " + md5);
		}  	
	}

	private void download(){
		String httpUrl = RomUpdate.getHttpUrl(mContext);
		String directUrl = RomUpdate.getDirectUrl(mContext);
		String error = getResources().getString(R.string.available_url_error);
		
		boolean isMobile = Utils.isMobileNetwork(mContext);
		boolean isSettingWiFiOnly = Preferences.getNetworkType(mContext).equals("2");
			
		if(isMobile && isSettingWiFiOnly){
			mNetworkDialog = new Builder(mContext);
			mNetworkDialog.setIconAttribute(R.attr.alertIcon)
			.setTitle(R.string.available_wrong_network_title)
			.setMessage(R.string.available_wrong_network_message)
			.setPositiveButton(R.string.ok, null)
			.show();
		} else {
			// We're good, open links or start downloads
			if(directUrl.equals("null") && !httpUrl.equals("null")){
				if(DEBUGGING)
					Log.d(TAG, "HTTP link opening");
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(httpUrl));
				startActivity(intent);
			} else if (directUrl.equals("null") && httpUrl.equals("null")){
				if(DEBUGGING)
					Log.e(TAG, "No links found");
				Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
			} else {
				if(DEBUGGING)
					Log.d(TAG, "Downloading via DownloadManager");
				DownloadRomUpdate.startDownload(mContext);
				setupUpdateNameInfo();
				invalidateOptionsMenu();
			}
		}
	}

	public static void setupProgress(){
		if(DEBUGGING)
			Log.d(TAG, "Setting up Progress Bars");
		boolean downloadFinished = Preferences.getDownloadFinished(mContext);
		if(downloadFinished){
			if(DEBUGGING)
				Log.d(TAG, "Download finished. Setting up Progress Bars accordingly.");
			String ready = mContext.getResources().getString(R.string.available_ready_to_install);

			int[] attr = new int[] { R.attr.colorHoloBlue };
			TypedArray ta = mContext.obtainStyledAttributes(attr);
			mProgressCounterText.setTextColor(ta.getColor(0, R.color.holo_blue_light));
			ta.recycle();
			mProgressCounterText.setText(ready);
			mProgressBar.setProgress(100);
		} else {
			if(DEBUGGING)
				Log.d(TAG, "Download not finished/started. Setting Progress Bars to default.");
			int fileSize = RomUpdate.getFileSize(mContext);
			String fileSizeStr = Utils.formatDataFromBytes(fileSize);
			mProgressCounterText.setText(fileSizeStr);
			mProgressBar.setProgress(0);
		}
	}

	public static void updateProgress(int progress, int downloaded, int total){
		mProgressBar.setProgress((int) progress);
		mProgressCounterText.setText(
				Utils.formatDataFromBytes(downloaded) + 
				"/" + 
				Utils.formatDataFromBytes(total));
	}

	public static void invalidateMenu(){
		((Activity) mContext).invalidateOptionsMenu();
	}

	public class MD5Check extends AsyncTask<Object, Boolean, Boolean>{

		public final String TAG = this.getClass().getSimpleName();

		Context mContext;
		ProgressDialog mMD5CheckDialog;

		public MD5Check(Context context){
			mContext = context;
		}

		@Override
		protected void onPreExecute(){
			// Setup Checking dialog
			mMD5CheckDialog = new ProgressDialog(mContext);
			mMD5CheckDialog.setCancelable(false);
			mMD5CheckDialog.setIndeterminate(true);
			mMD5CheckDialog.setMessage(mContext.getString(R.string.available_checking_md5));
			mMD5CheckDialog.show();
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			String file = RomUpdate.getFullFile(mContext).toString(); // Full file, with path
			String md5Remote = RomUpdate.getMd5(mContext); // Remote MD5 form the manifest. This is what we expect it to be
			String md5Local = Tools.noneRootShell("md5sum " + file + " | cut -d ' ' -f 1"); // Run the check on our local file
			md5Local = md5Local.trim(); // Trim both to remove any whitespace
			md5Remote = md5Remote.trim();			
			return md5Local.equals(md5Remote); // Return the comparison
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mMD5CheckDialog.cancel(); // Remove dialog

			// Show toast letting the user know immediately
			if(result){
				Toast.makeText(mContext, mContext.getString(R.string.available_md5_ok), Toast.LENGTH_LONG).show();           
			} else {
				Toast.makeText(mContext, mContext.getString(R.string.available_md5_failed), Toast.LENGTH_LONG).show();           
			}

			Preferences.setMD5Passed(mContext, result); // Set value for other persistent settings
			invalidateOptionsMenu(); // Reset options menu
			super.onPostExecute(result);
		}
	}
}
