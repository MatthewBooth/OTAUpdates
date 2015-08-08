package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.access.VersionAccess;
import com.ota.updates.items.VersionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VersionJSONParser extends BaseJSONParser {

    public static String TAG = VersionJSONParser.class.getName();

    public VersionJSONParser(Context context, String jsonString) {
        super(context, jsonString);
    }

    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

            JSONArray versionsArray = romObj.getJSONArray(NAME_VERSIONS);

            VersionAccess access = new VersionAccess(mContext);

            for (int i = 0; i < versionsArray.length(); i++) {
                JSONObject versionObj = versionsArray.getJSONObject(i);

                VersionItem versionItem = new VersionItem();
                versionItem.setId(versionObj.getInt(NAME_ID));
                versionItem.setDownloads(versionObj.getInt(NAME_DOWNLOADS));
                versionItem.setFullName(versionObj.getString(NAME_FULL_NAME));
                versionItem.setSlug(versionObj.getString(NAME_SLUG));
                versionItem.setAndroidVersion(versionObj.getString(NAME_ANDROID_VERSION));
                versionItem.setChangelog(versionObj.getString(NAME_CHANGELOG));
                versionItem.setUpdatedAt(versionObj.getString(NAME_UPDATED_AT));
                versionItem.setCreatedAt(versionObj.getString(NAME_CREATED_AT));
                versionItem.setPublishedAt(versionObj.getString(NAME_PUBLISHED_AT));
                versionItem.setVersionNumber(versionObj.getInt(NAME_VERSION_NUMBER));

                JSONObject deltaObj = versionObj.getJSONObject(NAME_DELTA_UPLOAD);
                JSONObject fullObj = versionObj.getJSONObject(NAME_FULL_UPLOAD);

                versionItem.setDeltaUploadId(deltaObj.getInt(NAME_ID));
                versionItem.setFullUploadId(fullObj.getInt(NAME_ID));

                new UploadJSONParser(mContext, deltaObj.toString());

                new UploadJSONParser(mContext, fullObj.toString());

                addToDatabase(versionItem, access);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
