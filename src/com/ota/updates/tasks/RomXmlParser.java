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

package com.ota.updates.tasks;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

import com.ota.updates.RomUpdate;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Utils;

public class RomXmlParser extends DefaultHandler implements Constants {

	public final String TAG = this.getClass().getSimpleName();

	String value = null;
	Context mContext;

	int filesize;
	String filesizeStr;

	boolean tagName = false;
	boolean tagVersion = false;
	boolean tagCode = false;
	boolean tagDirectUrl = false;
	boolean tagHttpUrl = false;
	boolean tagMD5 = false;
	boolean tagLog = false;
	boolean tagAndroid = false;
	boolean tagDeveloper = false;
	boolean tagWebsite = false;
	boolean tagDonateUrl = false;

	public void parse(File xmlFile, Context context) throws IOException {
		mContext = context;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFile, this);

			checkUpdateAvailability();

		} catch (ParserConfigurationException ex) {
			Log.e(TAG, "", ex);
		} catch (SAXException ex) {
			Log.e(TAG, "", ex);
		}
	}

	private void checkUpdateAvailability() {
		
		// Grab the data from the device and manifest
		String currentVersion = Utils.getProp("ro.ota.version");
		currentVersion = currentVersion.replaceAll("[^0-9]", ""); // Strip anything not a number
		String manifestVer = RomUpdate.getVersion(mContext);
		manifestVer = manifestVer.replaceAll("[^0-9]", "");

		// Parse ints
		int manifestNumber = Integer.parseInt(manifestVer);
		int currentNumber = Integer.parseInt(currentVersion);
		
		// Pad out to be at least 5 digits long
		// That way, 1.2 shows bigger than 1.1.1
		// Because 12000 > 11100
		// Without this it would show 12 > 111
		if(manifestVer.length() <= 2){
			manifestNumber *= 1000;
		} else if(manifestVer.length() <= 3) {
			manifestNumber *= 100;
		} else if(manifestVer.length() <= 4) {
			manifestNumber *= 10;
		}
		
		// Same again
		if(currentVersion.length() <= 2){
			currentNumber *= 1000;
		} else if(currentVersion.length() <= 3) {
			currentNumber *= 100;
		} else if(currentVersion.length() <= 4) {
			currentNumber *= 10;
		}

		RomUpdate.setUpdateAvailable(mContext, manifestNumber > currentNumber);
		if(DEBUGGING)
			Log.d(TAG, "Update Availability is " + (manifestNumber > currentNumber));
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (attributes.getLength() > 0) {

			@SuppressWarnings("unused")
			String tag = "<" + qName;
			for (int i = 0; i < attributes.getLength(); i++) {

				tag += " " + attributes.getLocalName(i) + "="
						+ attributes.getValue(i);
			}
			tag += ">";
		}

		if (qName.equalsIgnoreCase("name")) {
			tagName = true;
		}

		if (qName.equalsIgnoreCase("version")) {
			tagVersion = true;
		}

		if (qName.equalsIgnoreCase("code")) {
			tagCode = true;
		}

		if (qName.equalsIgnoreCase("directurl")) {
			tagDirectUrl = true;
		}
		
		if (qName.equalsIgnoreCase("httpurl")) {
			tagHttpUrl = true;
		}

		if (qName.equalsIgnoreCase("checkmd5")) {
			tagMD5 = true;
		}

		if (qName.equalsIgnoreCase("changelog")) {
			tagLog = true;
		}

		if (qName.equalsIgnoreCase("android")){
			tagAndroid = true;
		}
		
		if (qName.equalsIgnoreCase("website")){
			tagWebsite = true;
		}
		
		if (qName.equalsIgnoreCase("developer")){
			tagDeveloper = true;
		}
		
		if (qName.equalsIgnoreCase("donateurl")){
			tagDonateUrl = true;
		}

	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		value = new String(ch, start, length);
		if (tagName) {
			RomUpdate.setName(mContext, value);
			tagName = false;
			if(DEBUGGING)
				Log.d(TAG, "Name = " + value);
		}

		if (tagVersion) {
			RomUpdate.setVersion(mContext, value);
			tagVersion = false;
			if(DEBUGGING)
				Log.d(TAG, "Version = " + value);
		}

		if (tagCode) {
			RomUpdate.setCodename(mContext, value);
			tagCode = false;
			if(DEBUGGING)
				Log.d(TAG, "Codename = " + value);
		}

		if (tagDirectUrl) {
			RomUpdate.setDirectUrl(mContext, value);
			tagDirectUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "URL = " + value);
			URL url;
			try {
				url = new URL(value);
				URLConnection connection = url.openConnection();
				connection.connect();
				RomUpdate.setFileSize(mContext, connection.getContentLength());
				if(DEBUGGING)
					Log.d(TAG, "Remote Filesize = " + RomUpdate.getFileSize(mContext));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (tagHttpUrl) {
			RomUpdate.setHttpUrl(mContext, value);
			tagHttpUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "tagHttpUrl = " + value);
		}

		if (tagMD5) {
			RomUpdate.setMd5(mContext, value);
			tagMD5 = false;
			if(DEBUGGING)
				Log.d(TAG, "MD5 = " + value);
		}

		if (tagLog) {
			RomUpdate.setChangelog(mContext, value);
			tagLog = false;
			if(DEBUGGING)
				Log.d(TAG, "Changelog = " + value);
		}
		if (tagAndroid) {
			RomUpdate.setAndroidVersion(mContext, value);
			tagAndroid = false;
			if(DEBUGGING)
				Log.d(TAG, "Android Version = " + value);
		}
		
		if (tagWebsite){
			RomUpdate.setWebsite(mContext, value);
			tagWebsite = false;
			if(DEBUGGING)
				Log.d(TAG, "Website = " + value);
		}
		
		if(tagDeveloper){
			RomUpdate.setDeveloper(mContext, value);
			tagDeveloper = false;
			if(DEBUGGING)
				Log.d(TAG, "Developer = " + value);
		}
		if (tagDonateUrl){
			RomUpdate.setDonateLink(mContext, value);
			tagDonateUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "Donate URL = " + value);
		}
	}    
}
