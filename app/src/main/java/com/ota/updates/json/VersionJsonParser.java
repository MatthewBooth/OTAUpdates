package com.ota.updates.json;
/*
 * Copyright (C) 2015 Matt Booth.
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

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.VersionItem;
import com.ota.updates.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VersionJsonParser implements Constants {

    private static String TAG = VersionJsonParser.class.getName();

    private String mJSONString;
    private Context mContext;

    public VersionJsonParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    /**
     * Parse the Versions array within the selected JSON string
     */
    public boolean parse() {
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
                versionItem.setCreatedAt(versionObj.getString(NAME_CREATED_AT));
                versionItem.setPublishedAt(versionObj.getString(NAME_PUBLISHED_AT));
                versionItem.setVersionNumber(versionObj.getInt(NAME_VERSION_NUMBER));


                JSONObject deltaObj = versionObj.optJSONObject(NAME_DELTA_UPLOAD);
                if (deltaObj != null) {
                    versionItem.setDeltaUploadId(deltaObj.getInt(NAME_ID));
                    new UploadJsonParser(mContext, deltaObj.toString()).parse();
                } else {
                    versionItem.setDeltaUploadId(-1);
                }

                JSONObject fullObj = versionObj.getJSONObject(NAME_FULL_UPLOAD);
                versionItem.setFullUploadId(fullObj.getInt(NAME_ID));
                new UploadJsonParser(mContext, fullObj.toString()).parse();

                helper.addVersion(versionItem);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }
}
