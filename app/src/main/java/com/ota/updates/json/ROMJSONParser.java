package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ROMJSONParser extends BaseJSONParser {

    public static String TAG = ROMJSONParser.class.getName();

    public ROMJSONParser(Context context, String jsonString) {
        super(context, jsonString);
    }

    @Override
    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
