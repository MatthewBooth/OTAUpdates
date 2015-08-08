package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.access.AddonAccess;
import com.ota.updates.items.AddonItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddonJSONParser extends BaseJSONParser {

    public static String TAG = AddonJSONParser.class.getName();

    public AddonJSONParser(Context context, String jsonString) {
        super(context, jsonString);
    }

    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            JSONObject romObj = jObj.getJSONObject(NAME_ROM);

            JSONArray addonsArr = romObj.getJSONArray(NAME_ADDONS);

            AddonAccess access = new AddonAccess(mContext);

            for (int i = 0; i < addonsArr.length(); i++) {
                JSONObject versionObj = addonsArr.getJSONObject(i);

                AddonItem addonItem = new AddonItem();

                addonItem.setId(versionObj.getInt(NAME_ID));
                addonItem.setDownloads(versionObj.getInt(NAME_DOWNLOADS));
                addonItem.setName(versionObj.getString(NAME_NAME));
                addonItem.setSlug(versionObj.getString(NAME_SLUG));
                addonItem.setUpdatedAt(versionObj.getString(NAME_UPDATED_AT));
                addonItem.setCreatedAt(versionObj.getString(NAME_CREATED_AT));
                addonItem.setPublishedAt(versionObj.getString(NAME_PUBLISHED_AT));
                addonItem.setSize(versionObj.getInt(NAME_SIZE));
                addonItem.setMd5(versionObj.getString(NAME_MD5));
                addonItem.setDownloadLink(versionObj.getString(NAME_DOWNLOAD_LINK));
                addToDatabase(addonItem, access);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
