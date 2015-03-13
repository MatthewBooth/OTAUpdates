package com.ota.updates.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ota.updates.Addon;
import com.ota.updates.utils.Constants;

import android.content.Context;
import android.util.Log;

public class AddonXmlParser extends DefaultHandler implements Constants {

	private ArrayList<Addon> mAddons = new ArrayList<Addon>();
	private Addon mAddon;

	private final String TAG = this.getClass().getSimpleName();

	private StringBuffer value = new StringBuffer();
	private int id;

	boolean tagAddon = false;
	boolean tagId = false;
	boolean tagTitle = false;
	boolean tagDesc = false;
	boolean tagUpdatedAt = false;
	boolean tagSize = false;
	boolean tagDownloadLink = false;

	public ArrayList<Addon> parse(File xmlFile, Context context) throws IOException {

		try {
			id = 1;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFile, this);

			return mAddons;

		} catch (ParserConfigurationException ex) {
			Log.e(TAG, "", ex);
		} catch (SAXException ex) {
			Log.e(TAG, "", ex);
		}

		return null;
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

		if (qName.equalsIgnoreCase("addon")) {
			mAddon = new Addon();
			tagAddon = true;
		}
		
		if (qName.equalsIgnoreCase("id")) {
			tagId = true;
		}

		if (qName.equalsIgnoreCase("name")){
			tagTitle = true;
		}

		if (qName.equalsIgnoreCase("description")) {
			tagDesc = true;
		}

		if (qName.equalsIgnoreCase("updated-at")) {
			tagUpdatedAt = true;
		}
		
		if (qName.equalsIgnoreCase("size")) {
			tagSize = true;
		}
		
		if (qName.equalsIgnoreCase("download-link")) {
			tagDownloadLink = true;
		}
	}

	@Override
	public void characters(char[] buffer, int start, int length) 
			throws SAXException{
		value.append(buffer, start, length);

	}    

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		String input = value.toString().trim();

		if (tagAddon) {
			mAddons.add(mAddon);
			tagAddon = false;
		} else {
			
			if (tagId) {
				mAddon.setId(id);
				tagId = false;
				if (DEBUGGING) {
					Log.d(TAG, "Id = " + id);
				}
				id++;
			}

			if (tagTitle){
				mAddon.setTitle(input);
				tagTitle = false;
				if (DEBUGGING)
					Log.d(TAG, "Title = " + input);
			}

			if (tagDesc) {
				mAddon.setDesc(input);
				tagDesc = false;
				if (DEBUGGING)
					Log.d(TAG, "Description = " + input);
			}

			if (tagUpdatedAt) {
				String[] splitInput = input.split("T");
				mAddon.setUpdatedOn(splitInput[0]);
				tagUpdatedAt = false;
				if (DEBUGGING) {
					Log.d(TAG, "Updated Date = " + splitInput[0]);
				}    		
			}
			
			if (tagSize) {
				mAddon.setFilesize(Integer.parseInt(input));
				tagSize = false;
				if (DEBUGGING)
					Log.d(TAG, "Filesize " + Integer.parseInt(input));
			}
			
			if (tagDownloadLink) {
				mAddon.setDownloadLink(input);
				tagDownloadLink = false;
				if (DEBUGGING)
					Log.d(TAG, "Download Link = " + input);
			}
		}
	}
}