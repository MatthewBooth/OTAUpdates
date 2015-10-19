package com.ota.updates.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadJSON extends AsyncTask<String, Integer, Boolean> implements Constants {
    public final String TAG = this.getClass().getSimpleName();
    public AsyncResponse mResponse;
    private Context mContext;

    private String mUrl;
    private String mManifestFilename;

    public DownloadJSON(Context context, AsyncResponse response) {
        mContext = context;
        mResponse = response;
        mUrl = Utils.getProp(PROP_MANIFEST);
        mManifestFilename = getManifestFilename();
    }

    private String getManifestFilename() {
        String[] urlSplit = mUrl.split("/");
        int lastSplit = urlSplit.length - 1;
        return urlSplit[lastSplit];
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        try {
            InputStream input;

            URL url = new URL(mUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // download the file
            input = new BufferedInputStream(url.openStream());

            OutputStream output = mContext.openFileOutput(
                    mManifestFilename, Context.MODE_PRIVATE);

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            return true;
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResponse.processFinish(result);
    }
}
