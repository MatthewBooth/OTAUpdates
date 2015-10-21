package com.ota.updates.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.callbacks.AsyncResponse;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Utils;

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

        VersionSQLiteHelper versionSQLiteHelper = new VersionSQLiteHelper(mContext);
        Integer lastVersionNumber = versionSQLiteHelper.getLastVersionNumber();
        String remoteVersion = Integer.toString(lastVersionNumber);

        return Utils.getUpdateAvailability(mContext, remoteVersion);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResponse.processFinish(result);
    }
}


