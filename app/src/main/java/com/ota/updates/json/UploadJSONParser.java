package com.ota.updates.json;

import android.content.Context;
import android.util.Log;

import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.items.UploadItem;
import com.ota.updates.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadJSONParser implements Constants {

    private static String TAG = UploadJSONParser.class.getName();

    private String mJSONString;
    private Context mContext;
    private Type mType;

    public UploadJSONParser(Context context, String jsonString, Type type) {
        mJSONString = jsonString;
        mContext = context;
        mType = type;
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

            UploadSQLiteHelper helper = new UploadSQLiteHelper(mContext);
            helper.addUpload(uploadItem);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public enum Type {DELTA, FULL}
}
