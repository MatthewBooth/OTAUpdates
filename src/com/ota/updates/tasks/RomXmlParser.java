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

	boolean tagRomName = false;
	boolean tagVersionName = false;
	boolean tagVersionNumber = false;
	boolean tagDirectUrl = false;
	boolean tagHttpUrl = false;
	boolean tagMD5 = false;
	boolean tagLog = false;
	boolean tagAndroid = false;
	boolean tagDeveloper = false;
	boolean tagWebsite = false;
	boolean tagDonateUrl = false;
	boolean tagFileSize = false;
	boolean tagRomHut = false;

	public void parse(File xmlFile, Context context) throws IOException {
		mContext = context;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFile, this);

			Utils.setUpdateAvailability(context);

		} catch (ParserConfigurationException ex) {
			Log.e(TAG, "", ex);
		} catch (SAXException ex) {
			Log.e(TAG, "", ex);
		}
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

		if (qName.equalsIgnoreCase("romname")) {
			tagRomName = true;
		}

		if (qName.equalsIgnoreCase("versionname")) {
			tagVersionName = true;
		}
		
		if (qName.equalsIgnoreCase("versionnumber")){
			tagVersionNumber = true;
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

		if (qName.equalsIgnoreCase("websiteurl")){
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
		if (qName.equalsIgnoreCase("romhut")){
			tagRomHut = true;
		}

	}

	@Override
	public void characters(char[] buffer, int start, int length) 
			throws SAXException{
		value.append(buffer, start, length);;
		
	}    

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		String input = value.toString().trim();
		
		if (tagRomName) {
			RomUpdate.setRomName(mContext, input);
			tagRomName = false;
			if(DEBUGGING)
				Log.d(TAG, "Name = " + input);
		}

		if (tagVersionName) {
			RomUpdate.setVersionName(mContext, input);
			tagVersionName = false;
			if(DEBUGGING)
				Log.d(TAG, "Version = " + input);
		}
		
		if (tagVersionNumber){
			RomUpdate.setVersionNumber(mContext, Integer.parseInt(input));
			tagVersionNumber = false;
			if(DEBUGGING)
				Log.d(TAG, "OTA Version = " + input);
		}

		if (tagDirectUrl) {
			RomUpdate.setDirectUrl(mContext, input);
			tagDirectUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "URL = " + input);
			URL url;
			try {
				url = new URL(input);
				URLConnection connection = url.openConnection();
				connection.connect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (tagHttpUrl) {
			RomUpdate.setHttpUrl(mContext, input);
			tagHttpUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "tagHttpUrl = " + input);
		}

		if (tagMD5) {
			RomUpdate.setMd5(mContext, input);
			tagMD5 = false;
			if(DEBUGGING)
				Log.d(TAG, "MD5 = " + input);
		}

		if (tagLog) {
			RomUpdate.setChangelog(mContext, input);
			tagLog = false;
			if(DEBUGGING)
				Log.d(TAG, "Changelog = " + input);
		}
		if (tagAndroid) {
			RomUpdate.setAndroidVersion(mContext, input);
			tagAndroid = false;
			if(DEBUGGING)
				Log.d(TAG, "Android Version = " + input);
		}

		if (tagWebsite){
			RomUpdate.setWebsite(mContext, input);
			tagWebsite = false;
			if(DEBUGGING)
				Log.d(TAG, "Website = " + input);
		}

		if(tagDeveloper){
			RomUpdate.setDeveloper(mContext, input);
			tagDeveloper = false;
			if(DEBUGGING)
				Log.d(TAG, "Developer = " + input);
		}
		if (tagDonateUrl){
			RomUpdate.setDonateLink(mContext, input);
			tagDonateUrl = false;
			if(DEBUGGING)
				Log.d(TAG, "Donate URL = " + input);
		}
		if (tagFileSize){
			RomUpdate.setFileSize(mContext, Integer.parseInt(input));
			tagFileSize = false;
			if(DEBUGGING)
				Log.d(TAG, "Filesize = " + input);
		}
		if (tagRomHut){
			RomUpdate.setRomHut(mContext, input);
			tagRomHut = false;
			if(DEBUGGING)
				Log.d(TAG, "Romhut? = " + input);
		}

	}

}

