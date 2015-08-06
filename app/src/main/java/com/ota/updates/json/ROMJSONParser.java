package com.ota.updates.json;

import android.content.Context;

import com.ota.updates.db.Contracts;
import com.ota.updates.items.AddonItem;
import com.ota.updates.items.UploadItem;
import com.ota.updates.items.VersionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ROMJSONParser extends BaseJSONParser {

    private String mJSONString;
    private Context mContext;

    public ROMJSONParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
    }

    @Override
    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);
            JSONObject romObj = jObj.getJSONObject(COLUMN_NAME_ROM);

            JSONArray versionsArray = romObj.getJSONArray(COLUMN_NAME_VERSIONS);

            parseVersions(versionsArray);

            JSONArray addonsArr = romObj.getJSONArray(COLUMN_NAME_ADDONS);

            parseAddons(addonsArr);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void parseAddons(JSONArray addonsArr) throws JSONException {
        for (int i = 0; i < addonsArr.length(); i++) {
            JSONObject versionObj = addonsArr.getJSONObject(i);

            AddonItem addonItem = new AddonItem();

            addonItem.setId(versionObj.getInt(Contracts.COLUMN_NAME_ID));
            addonItem.setDownloads(versionObj.getInt(Contracts.COLUMN_NAME_DOWNLOADS));
            addonItem.setName(versionObj.getString(Contracts.COLUMN_NAME_NAME));
            addonItem.setSlug(versionObj.getString(Contracts.COLUMN_NAME_SLUG));
            addonItem.setUpdatedAt(versionObj.getString(Contracts.COLUMN_NAME_UPDATED_AT));
            addonItem.setCreatedAt(versionObj.getString(Contracts.COLUMN_NAME_CREATED_AT));
            addonItem.setPublishedAt(versionObj.getString(Contracts.COLUMN_NAME_PUBLISHED_AT));
            addonItem.setSize(versionObj.getInt(Contracts.COLUMN_NAME_SIZE));
            addonItem.setMd5(versionObj.getString(Contracts.COLUMN_NAME_MD5));
            addonItem.setDownloadLink(versionObj.getString(Contracts.COLUMN_NAME_DOWNLOAD_LINK));

        }
    }

    public void parseVersions(JSONArray versionsArray) throws JSONException {
        for (int i = 0; i < versionsArray.length(); i++) {
            JSONObject versionObj = versionsArray.getJSONObject(i);

            VersionItem versionItem = new VersionItem();
            versionItem.setId(versionObj.getInt(Contracts.COLUMN_NAME_ID));
            versionItem.setDownloads(versionObj.getInt(Contracts.COLUMN_NAME_DOWNLOADS));
            versionItem.setFullName(versionObj.getString(Contracts.COLUMN_NAME_FULL_NAME));
            versionItem.setSlug(versionObj.getString(Contracts.COLUMN_NAME_SLUG));
            versionItem.setAndroidVersion(versionObj.getString(Contracts.COLUMN_NAME_ANDROID_VERSION));
            versionItem.setChangelog(versionObj.getString(Contracts.COLUMN_NAME_CHANGELOG));
            versionItem.setUpdatedAt(versionObj.getString(Contracts.COLUMN_NAME_UPDATED_AT));
            versionItem.setCreatedAt(versionObj.getString(Contracts.COLUMN_NAME_CREATED_AT));
            versionItem.setPublishedAt(versionObj.getString(Contracts.COLUMN_NAME_PUBLISHED_AT));
            versionItem.setVersionNumber(versionObj.getInt(Contracts.COLUMN_NAME_VERSION_NUMBER));

            JSONObject deltaObj = versionObj.getJSONObject(COLUMN_NAME_DELTA_UPLOAD);
            JSONObject fullObj = versionObj.getJSONObject(COLUMN_NAME_FULL_UPLOAD);

            versionItem.setDeltaUploadId(deltaObj.getInt(Contracts.COLUMN_NAME_ID));
            versionItem.setFullUploadId(fullObj.getInt(Contracts.COLUMN_NAME_ID));

            parseUpload(deltaObj);

            parseUpload(fullObj);

        }
    }

    public void parseUpload(JSONObject deltaObj) throws JSONException {
        UploadItem deltaItem = new UploadItem();
        deltaItem.setId(deltaObj.getInt(Contracts.COLUMN_NAME_ID));
        deltaItem.setDownloads(deltaObj.getInt(Contracts.COLUMN_NAME_DOWNLOADS));
        deltaItem.setSize(deltaObj.getInt(Contracts.COLUMN_NAME_SIZE));
        deltaItem.setStatus(deltaObj.getString(Contracts.COLUMN_NAME_STATUS));
        deltaItem.setMd5(deltaObj.getString(Contracts.COLUMN_NAME_MD5));
        deltaItem.setDownloadLink(deltaObj.getString(Contracts.COLUMN_NAME_DOWNLOAD_LINK));
    }

    @Override
    public void addToDatabase() {

    }
}
