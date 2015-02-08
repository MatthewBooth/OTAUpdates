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
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;
import com.ota.updates.R;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.Utils;

public class AboutActivity extends Activity implements OnClickListener{
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		Context context = this;
		setTheme(Preferences.getTheme(context));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ota_about);
		

		if(Utils.isLollipop()){
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
			setActionBar(toolbar);
			toolbar.setTitle(getResources().getString(R.string.app_name));
		}
		
		Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
		
		TextView aboutTitle = (TextView) findViewById(R.id.about_tv_about_title);
		TextView donateTitle = (TextView) findViewById(R.id.about_tv_donate_title);
		TextView creditsTitle = (TextView) findViewById(R.id.about_tv_credits_title);
		TextView creditsSummary = (TextView) findViewById(R.id.about_tv_credits_summary);
		Button donateButton = (Button) findViewById(R.id.about_btn_donate);
		
		aboutTitle.setTypeface(typeFace);
		donateTitle.setTypeface(typeFace);
		creditsTitle.setTypeface(typeFace);
		
		String openHTML = "";
		if(Utils.isLollipop()){
			openHTML = "<font color='#009688'>";
		} else {
			openHTML = "<font color='#33b5e5'>";
		}
        String closeHTML = "</font>";
        String newLine = "<br />";
        String creditsText =
                openHTML + "Matt Booth" + closeHTML + " - Anything not mentioned below" + newLine +
                openHTML + "Roman Nurik" + closeHTML + " - Android Asset Studio Framework" + newLine +
                openHTML + "Jeff Gilfelt"+ closeHTML + " - Android Action Bar Style Generator" + newLine + 
                openHTML + "Brad Greco" + closeHTML + " - DirectoryPicker" + newLine +
                openHTML + "Ficeto (AllianceROM)" + closeHTML + " - Shell tools" + newLine +
                openHTML + "StackOverflow" + closeHTML + " - Many, many people";
        creditsSummary.setText(Html.fromHtml(creditsText));
        
        donateButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {		
		String url = "http://goo.gl/ZKSY4";
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
}
