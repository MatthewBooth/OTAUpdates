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

	StringBuffer value = new StringBuffer();;
	Context mContext;

	int filesize;
	String filesizeStr;

	boolean tagName = false;
	boolean tagVersion = false;
	boolean tagDirectUrl = false;
	boolean tagHttpUrl = false;
	boolean tagMD5 = false;
	boolean tagLog = false;
	boolean tagAndroid = false;
	boolean tagDeveloper = false;
	boolean tagWebsite = false;
	boolean tagDonateUrl = false;
	boolean tagFileSize = false;
	boolean tagOtaVersion = false;

	public void parse(File xmlFile, Context context) throws IOException {
		mContext = context;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFile, this);

			setUpdateAvailability();

		} catch (ParserConfigurationException ex) {
			Log.e(TAG, "", ex);
		} catch (SAXException ex) {
			Log.e(TAG, "", ex);
		}
	}

	private boolean versionBiggerThan(String current, String manifest) {
		// returns true if current > manifest, false otherwise
		if (current.length() > manifest.length()) {
			for (int i = 0; i < current.length() - manifest.length(); i++) {
				manifest+="0";
			}
		} else if (manifest.length() > current.length()) {
			for (int i = 0; i < manifest.length() - current.length(); i++) {
				current+="0";
			}
		}

		for (int i = 0; i <= current.length(); i++) {
			if (current.charAt(i) > manifest.charAt(i)){
				return true; // definitely true
			} else if (manifest.charAt(i) > current.charAt(i)) {
				return false; // definitely false
			} else {
				//else still undecided
			}
		}
		return false;
	}

	private void setUpdateAvailability() {
		// Grab the data from the device and manifest
		int otaVersion = RomUpdate.getOtaVersion(mContext);
		String currentVer = Utils.getProp("ro.ota.version");
		String manifestVer = Integer.toString(otaVersion);

		boolean available = !versionBiggerThan(currentVer, manifestVer);

		RomUpdate.setUpdateAvailable(mContext, available);
		if(DEBUGGING)
			Log.d(TAG, "Update Availability is " + available);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		value.setLength(0);

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

		if (qName.equalsIgnoreCase("filesize")){
			tagFileSize = true;
		}

		if (qName.equalsIgnoreCase("ota-version")){
			tagOtaVersion = true;
		}

	}

	@Override
	public void characters(char[] buffer, int start, int length) 
			throws SAXException{
		value.append(buffer, start, length);;
		
	}    

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if (tagName) {
			RomUpdate.setName(mContext, value.toString());
			tagName = false;
			if(DEBUGGING)
				Log.d(TAG, "Name = " + value.toString());
		}

		if (tagVersion) {
			RomUpdate.setVersion(mContext, value.toString());
			tagVersion = false;
			if(DEBUGGING)
				Log.d(TAG, "Version = " + value.toString());
		}

		if (tagDirectUrl) {
			RomUpdate.setDirectUrl(mContext, value.toString());
			tagDirectUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "URL = " + value.toString());
			URL url;
			try {
				url = new URL(value.toString());
				URLConnection connection = url.openConnection();
				connection.connect();
				if(DEBUGGING)
					Log.d(TAG, "Remote Filesize = " + RomUpdate.getFileSize(mContext));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (tagHttpUrl) {
			RomUpdate.setHttpUrl(mContext, value.toString());
			tagHttpUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "tagHttpUrl = " + value.toString());
		}

		if (tagMD5) {
			RomUpdate.setMd5(mContext, value.toString());
			tagMD5 = false;
			if(DEBUGGING)
				Log.d(TAG, "MD5 = " + value.toString());
		}

		if (tagLog) {
			RomUpdate.setChangelog(mContext, value.toString());
			tagLog = false;
			if(DEBUGGING)
				Log.d(TAG, "Changelog = " + value.toString());
		}
		if (tagAndroid) {
			RomUpdate.setAndroidVersion(mContext, value.toString());
			tagAndroid = false;
			if(DEBUGGING)
				Log.d(TAG, "Android Version = " + value.toString());
		}

		if (tagWebsite){
			RomUpdate.setWebsite(mContext, value.toString());
			tagWebsite = false;
			if(DEBUGGING)
				Log.d(TAG, "Website = " + value.toString());
		}

		if(tagDeveloper){
			RomUpdate.setDeveloper(mContext, value.toString());
			tagDeveloper = false;
			if(DEBUGGING)
				Log.d(TAG, "Developer = " + value.toString());
		}
		if (tagDonateUrl){
			RomUpdate.setDonateLink(mContext, value.toString());
			tagDonateUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "Donate URL = " + value.toString());
		}
		if (tagFileSize){
			RomUpdate.setFileSize(mContext, Integer.parseInt(value.toString()));
			tagFileSize = false;
			if(DEBUGGING)
				Log.d(TAG, "Filesize = " + value);
		}
		if (tagOtaVersion){
			RomUpdate.setOtaVersion(mContext, Integer.parseInt(value.toString()));
			tagOtaVersion = false;
			if(DEBUGGING)
				Log.d(TAG, "OTA Version = " + value.toString());
		}

	}

}

