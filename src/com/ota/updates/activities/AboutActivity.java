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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ota.updates.R;
import com.ota.updates.tasks.Changelog;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Utils;

public class AboutActivity extends Activity {

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
				int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
				if (selectedPosition == 0) {
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

	public void openAppDonate(View v) {
		setupDonateDialog();
	}
	
	public void openChangelog(View v) {
		String title = getResources().getString(R.string.changelog);
		String changelog = getResources().getString(R.string.changelog_url);
		new Changelog(this, mContext, title, changelog, true).execute();
	}
}
