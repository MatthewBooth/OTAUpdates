package com.ota.updates.json;

import android.util.Log;

import com.ota.updates.db.access.VersionAccess;
import com.ota.updates.items.VersionItem;
import com.ota.updates.json.BaseJSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VersionJSONParser extends BaseJSONParser<VersionItem, VersionAccess> {

    public static String TAG = "VersionJSONParser";

    public VersionJSONParser(String data) {
        super(data, TAG);
    }

    public void parse() {
        try {
            JSONArray versionsArray = mJSONObject.getJSONArray("versions");
            for (int i = 0; i < versionsArray.length(); i++) {
                JSONObject version = versionsArray.getJSONObject(i);
                parseItem(version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: add to database here
    }

    @Override
    public void parseItem(JSONObject jsonObject) {
        VersionItem versionItem = new VersionItem();
        try {
            versionItem.setId(jsonObject.getInt("id"));
            versionItem.setDownloads(jsonObject.getInt("downloads"));
            versionItem.setFullName(jsonObject.getString("full_name"));
            versionItem.setSlug(jsonObject.getString("slug"));
            versionItem.setAndroidVersion(jsonObject.getString("android_version"));
            versionItem.setChangelog(jsonObject.getString("changelog"));
            versionItem.setUpdatedAt(jsonObject.getString("updated_at"));
            versionItem.setCreatedAt(jsonObject.getString("created_at"));
            versionItem.setPublishedAt(jsonObject.getString("published_at"));
            versionItem.setVersionNumber(jsonObject.getInt("version_number"));
            // TODO: Add full upload and delta upload here

            mItem.add(versionItem);

        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void addToDatabase(VersionAccess accessObject) {

    }
}
