package com.ota.updates.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ota.updates.callbacks.AsyncResponse;
import com.ota.updates.json.AddonJSONParser;
import com.ota.updates.json.VersionJSONParser;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Utils;

import java.io.File;
import java.io.IOException;

public class ParseJson extends AsyncTask<String, Integer, Boolean> implements Constants {

    public final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    public AsyncResponse mResponse;

    public ParseJson(Context context, AsyncResponse response) {
        mContext = context;
        mResponse = response;
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        // So, where's that file we just downloaded?
        final String manifestFilename = Utils.getManifestFilename();
        File jsonFile = new File(mContext.getApplicationContext().getFilesDir(), manifestFilename);

        String json;
        try {
            json = Utils.getFileContents(jsonFile);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            return false;
        }

        if (json != null) {
            // Parse and populate our Database
            boolean versionParsed = new VersionJSONParser(mContext, json).parse();
            boolean addonParsed = new AddonJSONParser(mContext, json).parse();

            if (DEBUGGING) {
                Log.d(TAG, "JSON data parsed status = " + (versionParsed && addonParsed));
            }

            return versionParsed && addonParsed;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResponse.processFinish(result);
    }
}
