package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddonJSONParser implements Constants {
    private static String TAG = AddonJSONParser.class.getName();

    private String mJSONString;
    private Context mContext;

    public AddonJSONParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    public void parse() {
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
                addonItem.setUpdatedAt(versionObj.getString(NAME_UPDATED_AT));
                addonItem.setCreatedAt(versionObj.getString(NAME_CREATED_AT));
                addonItem.setPublishedAt(versionObj.getString(NAME_PUBLISHED_AT));
                addonItem.setSize(versionObj.getInt(NAME_SIZE));
                addonItem.setMd5(versionObj.getString(NAME_MD5));
                addonItem.setDownloadLink(versionObj.getString(NAME_DOWNLOAD_LINK));
                helper.addAddon(addonItem);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
