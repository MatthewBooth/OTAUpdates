package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.VersionItem;
import com.ota.updates.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VersionJSONParser implements Constants {

    private static String TAG = VersionJSONParser.class.getName();

    private String mJSONString;
    private Context mContext;

    public VersionJSONParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

            JSONArray versionsArray = romObj.getJSONArray(NAME_VERSIONS);

            VersionSQLiteHelper helper = new VersionSQLiteHelper(mContext);

            for (int i = 0; i < versionsArray.length(); i++) {
                JSONObject arrayObj = versionsArray.getJSONObject(i);

                JSONObject versionObj = arrayObj.getJSONObject(NAME_VERSION);

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


                JSONObject deltaObj = versionObj.optJSONObject(NAME_DELTA_UPLOAD);
                if (deltaObj != null) {
                    versionItem.setDeltaUploadId(deltaObj.getInt(NAME_ID));
                    new UploadJSONParser(mContext, deltaObj.toString(), UploadJSONParser.Type.DELTA).parse();
                } else {
                    versionItem.setDeltaUploadId(-1);
                }

                JSONObject fullObj = versionObj.getJSONObject(NAME_FULL_UPLOAD);
                versionItem.setFullUploadId(fullObj.getInt(NAME_ID));
                new UploadJSONParser(mContext, fullObj.toString(), UploadJSONParser.Type.FULL).parse();

                helper.addVersion(versionItem);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
