package com.ota.updates.tasks;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.ota.updates.Addon;
import com.ota.updates.utils.Constants;

public class AddonXmlParser extends DefaultHandler implements Constants {

	private final static String TAG = "AddonXmlParser";

	public static ArrayList<Addon> parse(File xmlFile) {
		ArrayList<Addon> addons = null;
        try {
            SAXParserFactory xmlReader = SAXParserFactory.newInstance();
            SAXParser saxParser = xmlReader.newSAXParser();
            AddonsSaxHandler saxHandler = new AddonsSaxHandler();
            saxParser.parse(xmlFile, saxHandler);
            addons = saxHandler.getAddons();
        } catch (Exception ex) {
            Log.d(TAG, "SAXXMLParser: parse() failed");
        }
        return addons;
    }
}