package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.access.UploadAccess;
import com.ota.updates.items.UploadItem;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadJSONParser extends BaseJSONParser {

    public static String TAG = UploadJSONParser.class.getName();

    public UploadJSONParser(Context context, String jsonString) {
        super(context, jsonString);
    }

    public void parse() {
        try {
            JSONObject jObj = new JSONObject(mJSONString);

            UploadItem uploadItem = new UploadItem();
            uploadItem.setId(jObj.getInt(NAME_ID));
            uploadItem.setDownloads(jObj.getInt(NAME_DOWNLOADS));
            uploadItem.setSize(jObj.getInt(NAME_SIZE));
            uploadItem.setStatus(jObj.getString(NAME_STATUS));
            uploadItem.setMd5(jObj.getString(NAME_MD5));
            uploadItem.setDownloadLink(jObj.getString(NAME_DOWNLOAD_LINK));
            UploadAccess access = new UploadAccess(mContext);
            addToDatabase(uploadItem, access);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
