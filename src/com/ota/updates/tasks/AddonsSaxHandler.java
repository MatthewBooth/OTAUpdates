package com.ota.updates.tasks;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.ota.updates.Addon;
import com.ota.updates.utils.Constants;

public class AddonsSaxHandler extends DefaultHandler implements Constants {
	
	private final String TAG = this.getClass().getSimpleName();
	
	private ArrayList<Addon> addons;
    private String tempVal;
    private Addon tempAddon;
 
    public AddonsSaxHandler() {
        addons = new ArrayList<Addon>();
    }
 
    public ArrayList<Addon> getAddons() {
        return addons;
    }
 
    // Event Handlers
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        // reset
        tempVal = "";
        if (qName.equalsIgnoreCase("addon")) {
            // create a new instance of employee
            tempAddon = new Addon();
        }
    }
 
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tempVal = new String(ch, start, length);
    }
 
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("addon")) {
            // add it to the list
            addons.add(tempAddon);
        } else if (qName.equalsIgnoreCase("name")) {
            tempAddon.setTitle(tempVal);
            if (DEBUGGING)
				Log.d(TAG, "Title = " + tempVal);
        } else if (qName.equalsIgnoreCase("description")) {
            tempAddon.setDesc(tempVal);
            if (DEBUGGING)
				Log.d(TAG, "Desc = " + tempVal);
        } else if (qName.equalsIgnoreCase("published-at")) {
        	String[] splitInput = tempVal.split("T");
            tempAddon.setPublishedAt(splitInput[0]);
            if (DEBUGGING)
				Log.d(TAG, "Updated On = " + tempVal);
        } else if (qName.equalsIgnoreCase("download-link")) {
            tempAddon.setDownloadLink(tempVal);
            if (DEBUGGING)
				Log.d(TAG, "Download Link = " + tempVal);
        } else if (qName.equalsIgnoreCase("size")) {
            tempAddon.setFilesize(Integer.parseInt(tempVal));
            if (DEBUGGING)
				Log.d(TAG, "Size = " + tempVal);
        } else if (qName.equalsIgnoreCase("id")) {
            tempAddon.setId(Integer.parseInt(tempVal));
            if (DEBUGGING)
				Log.d(TAG, "Id = " + tempVal);
        }
    }

}
