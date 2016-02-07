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

import com.ota.updates.db.helpers.RomSQLiteHelper;
import com.ota.updates.items.RomItem;
import com.ota.updates.utils.Fields;

import org.json.JSONException;
import org.json.JSONObject;

public class RomJsonParser implements Fields {

    private static final String TAG = RomJsonParser.class.getName();

    private String mJSONString;
    private Context mContext;

    public RomJsonParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    /**
     * Parse the Rom object within the selected JSON string
     */
    public boolean parse() {
        try {
            RomItem romItem = new RomItem();
            JSONObject jObj = new JSONObject(mJSONString);
            RomSQLiteHelper helper = new RomSQLiteHelper(mContext);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

            romItem.setId(romObj.getInt(NAME_ID));
            romItem.setName(romObj.getString(NAME_NAME));
            romItem.setSlug(romObj.getString(NAME_SLUG));
            romItem.setDescription(romObj.getString(NAME_DESCRIPTION));
            romItem.setPublishedAt(romObj.getString(NAME_PUBLISHED_AT));
            romItem.setCreatedAt(romObj.getString(NAME_CREATED_AT));
            romItem.setDownloads(romObj.getInt(NAME_DOWNLOADS));
            romItem.setWebsiteUrl(romObj.getString(NAME_WEBSITE_URL));
            romItem.setDonateUrl(romObj.getString(NAME_DONATE_URL));

            helper.addRom(romItem);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }
}
