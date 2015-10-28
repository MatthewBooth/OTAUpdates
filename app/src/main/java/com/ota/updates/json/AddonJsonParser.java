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

import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddonJsonParser implements Constants {
    private static String TAG = AddonJsonParser.class.getName();

    private String mJSONString;
    private Context mContext;

    public AddonJsonParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    /**
     * Parse the Addons array within the selected JSON string
     */
    public boolean parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

            JSONArray addonsArr = romObj.getJSONArray(NAME_ADDONS);

            AddonSQLiteHelper helper = new AddonSQLiteHelper(mContext);

            for (int i = 0; i < addonsArr.length(); i++) {
                JSONObject arrayObj = addonsArr.getJSONObject(i);

                JSONObject versionObj = arrayObj.getJSONObject(NAME_ADDON);

                AddonItem addonItem = new AddonItem();

                addonItem.setId(versionObj.getInt(NAME_ID));
                addonItem.setDownloads(versionObj.getInt(NAME_DOWNLOADS));
                addonItem.setName(versionObj.getString(NAME_NAME));
                addonItem.setSlug(versionObj.getString(NAME_SLUG));
                addonItem.setDescription(versionObj.getString(NAME_DESCRIPTION));
                addonItem.setCreatedAt(versionObj.getString(NAME_CREATED_AT));
                addonItem.setPublishedAt(versionObj.getString(NAME_PUBLISHED_AT));
                addonItem.setSize(versionObj.getInt(NAME_SIZE));
                addonItem.setMd5(versionObj.getString(NAME_MD5));
                addonItem.setDownloadLink(versionObj.getString(NAME_DOWNLOAD_LINK));
                addonItem.setCategory(versionObj.getString(NAME_CATEGORY));
                helper.addAddon(addonItem);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }
}
