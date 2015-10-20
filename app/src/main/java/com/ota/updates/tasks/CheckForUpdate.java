package com.ota.updates.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.json.AddonJSONParser;
import com.ota.updates.json.VersionJSONParser;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Utils;

import java.io.File;
import java.io.IOException;

public class CheckForUpdate extends AsyncTask<String, Integer, Boolean> implements Constants {
    public final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    public AsyncResponse mResponse;

    public CheckForUpdate(Context context, AsyncResponse response) {
        mContext = context;
        mResponse = response;
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        boolean manifestParsed =false;
        try {
            manifestParsed = parseManifestJson();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
         return manifestParsed && checkUpdate();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResponse.processFinish(result);
    }

    private boolean parseManifestJson() throws IOException {
        // So, where's that file we just downloaded?
        final String manifestFilename = Utils.getManifestFilename();
        File jsonFile = new File(mContext.getApplicationContext().getFilesDir(), manifestFilename);

        String json = Utils.getFileContents(jsonFile);

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

    private boolean checkUpdate() {
        VersionSQLiteHelper versionSQLiteHelper = new VersionSQLiteHelper(mContext);
        Integer lastVersionNumber = versionSQLiteHelper.getLastVersionNumber();
        String remoteVersion = Integer.toString(lastVersionNumber);

        return Utils.getUpdateAvailability(mContext, remoteVersion);
    }
}


