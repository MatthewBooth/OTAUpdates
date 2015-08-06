package com.ota.updates.json;

import android.util.Log;

import com.ota.updates.db.access.UploadAccess;
import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.items.UploadItem;
import com.ota.updates.json.BaseJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadJSONParser extends BaseJSONParser<UploadItem, UploadAccess> {

    public static String TAG = "UploadJSONParser";

    public UploadJSONParser(String data) {
        super(data, TAG);
    }

    public void parse(JSONObject upload) {
        UploadItem uploadItem = new UploadItem();
        try {
            uploadItem.setId(upload.getInt("id"));
            uploadItem.setDownloads(upload.getInt("downloads"));
            uploadItem.setSize(upload.getInt("size"));
            uploadItem.setMd5(upload.getString("md5"));
            uploadItem.setStatus(upload.getString("status"));
            uploadItem.setDownloadLink(upload.getString("download_link"));
            mItem.add(uploadItem);

        } catch (JSONException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void parseItem(JSONObject jsonObject) {

    }

    @Override
    public void addToDatabase(UploadAccess accessObject) {

    }
}
