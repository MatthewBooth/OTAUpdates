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

import com.ota.updates.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ROMJSONParser implements Constants {

    private static final String TAG = ROMJSONParser.class.getName();

    private String mJSONString;
    private Context mContext;

    public ROMJSONParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    /**
     * Parse the Rom object within the selected JSON string
     */
    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
