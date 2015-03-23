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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ota.updates.R;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Utils;

public class AboutActivity extends Activity {
	
	private AdView mAdView;
	private Context mContext;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		setTheme(Preferences.getTheme(mContext));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ota_about);


		if (Utils.isLollipop()) {
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
			setActionBar(toolbar);
			toolbar.setTitle(getResources().getString(R.string.app_name));
		}

		Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

		TextView donateTitle = (TextView) findViewById(R.id.about_tv_donate_title);
		TextView creditsTitle = (TextView) findViewById(R.id.about_tv_credits_title);
		TextView changelogTitle = (TextView) findViewById(R.id.about_tv_changelog_title);
		TextView creditsSummary = (TextView) findViewById(R.id.about_tv_credits_summary);

		donateTitle.setTypeface(typeFace);
		changelogTitle.setTypeface(typeFace);
		creditsTitle.setTypeface(typeFace);

		String openHTML = "";
		if (Utils.isLollipop()) {
			if (Preferences.getCurrentTheme(this) == 0) { // Light
				openHTML = "<font color='#009688'>";
			} else {
				openHTML = "<font color='#80cbc4'>";
			}
		} else {
			openHTML = "<font color='#33b5e5'>";
		}
		String closeHTML = "</font>";
		String newLine = "<br />";
		String creditsText =
				openHTML + "Matt Booth" + closeHTML + " - Anything not mentioned below" + newLine +
				openHTML + "Roman Nurik" + closeHTML + " - Android Asset Studio Framework" + newLine +
				openHTML + "Jeff Gilfelt"+ closeHTML + " - Android Action Bar Style Generator" + newLine + 
				openHTML + "Ficeto (AllianceROM)" + closeHTML + " - Shell tools" + newLine +
				openHTML + "StackOverflow" + closeHTML + " - Many, many people";
		creditsSummary.setText(Html.fromHtml(creditsText));
		
		TextView versionTitle = (TextView) findViewById(R.id.about_tv_version_title);
		versionTitle.setTypeface(typeFace);
		
		TextView versionSummary = (TextView) findViewById(R.id.about_tv_version_summary);
		String appVer = getResources().getString(R.string.about_app_version);
		String appVerActual = getResources().getString(R.string.app_version);
		versionSummary.setText(appVer + " v" + appVerActual);
		
		if (Preferences.getAdsEnabled(this)) {
			mAdView = (AdView) findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}
	}

	private void setupDonateDialog() {
		String[] items = { "PayPal", "BitCoin" };
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("")
		.setSingleChoiceItems(items, 0, null)
		.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String url = "";
				if (which == 0) {
					url = "http://goo.gl/ZKSY4";
				} else {
					url = "http://goo.gl/o4c6ES";
				}
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		})
		.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.show();
	}

	@SuppressLint("InflateParams") 
	private void showChangelogDialog(String changelogText) {
		View view = getLayoutInflater().inflate(R.layout.ota_changelog_layout, null);
		TextView changelog = (TextView) view.findViewById(R.id.changelog);
		
		Bypass bypass = new Bypass(mContext);
		CharSequence string = bypass.markdownToSpannable(changelogText);
		changelog.setText(string);
		
		Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(R.string.changelog);
		dialog.setView(view);
		dialog.setPositiveButton(R.string.done, null);
		dialog.show();
	}
	
	public void openAppDonate(View v) {
		setupDonateDialog();
	}
	
	public void openChangelog(View v) {
		new Changelog().execute();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mAdView != null) {
			mAdView.pause();
		}
	}
	
	public class Changelog extends AsyncTask<Void, Void, String> {
		
		private ProgressDialog mLoadingDialog;
		private static final String CHANGELOG = "Changelog.md";
		private static final String TAG = "AboutActivity.Changelog";
		private File mChangelogFile;
		
		@Override
		protected void onPreExecute(){

			// Show a loading/progress dialog while the search is being performed
			mLoadingDialog = new ProgressDialog(mContext);
			mLoadingDialog.setIndeterminate(true);
			mLoadingDialog.setCancelable(false);
			mLoadingDialog.setMessage(mContext.getResources().getString(R.string.loading));
			mLoadingDialog.show();

			// Delete any existing manifest file before we attempt to download a new one
			mChangelogFile = new File(mContext.getFilesDir().getPath(), CHANGELOG);
			if(mChangelogFile.exists()) {
				mChangelogFile.delete();
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				InputStream input = null;

				String urlStr = "https://raw.githubusercontent.com/Kryten2k35/OTAUpdates/stable/Changelog.md";
				URL url = new URL(urlStr);
				URLConnection connection = url.openConnection();
				connection.connect();
				// download the file
				input = new BufferedInputStream(url.openStream());

				OutputStream output = mContext.openFileOutput(
						CHANGELOG, Context.MODE_PRIVATE);

				byte data[] = new byte[1024];
				int count;
				while ((count = input.read(data)) != -1) {
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();

				// file finished downloading, parse it!

			} catch (Exception e) {
				Log.d(TAG, "Exception: " + e.getMessage());
			}
			
			InputStreamReader inputReader = null;
	        String text = null;

	        try {
	            StringBuilder data = new StringBuilder();
	            char tmp[] = new char[2048];
	            int numRead;
	            inputReader = new FileReader(mChangelogFile);
	            while ((numRead = inputReader.read(tmp)) >= 0) {
	                data.append(tmp, 0, numRead);
	            }
	            text = data.toString();
	        } catch (IOException e) {
	            text = getString(R.string.changelog_error);
	        } finally {
	            try {
	                if (inputReader != null) {
	                    inputReader.close();
	                }
	            } catch (IOException e) {
	            }
	        }
			return text;
		}
		
		@Override
		protected void onPostExecute(String result) {
			mLoadingDialog.cancel();
			showChangelogDialog(result);
			super.onPostExecute(result);
		}
		
	}
}
