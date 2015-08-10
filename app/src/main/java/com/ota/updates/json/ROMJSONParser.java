package com.ota.updates.json;

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
