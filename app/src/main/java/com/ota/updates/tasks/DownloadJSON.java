package com.ota.updates.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ota.updates.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadJSON extends AsyncTask<String, Integer, Boolean> {
    private static final String MANIFEST = "aosp-jf.json";
    public final String TAG = this.getClass().getSimpleName();
    public AsyncResponse mResponse;
    String URL = "https://romhut.com/roms/";
    private Context mContext;
    private ProgressDialog mLoadingDialog;

    public DownloadJSON(Context context, AsyncResponse response) {
        mContext = context;
        mResponse = response;
    }

    @Override
    protected void onPreExecute() {
        mLoadingDialog = new ProgressDialog(mContext);
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setMessage(mContext.getResources().getString(R.string.loading));
        mLoadingDialog.show();
    }

    @Override
    protected Boolean doInBackground(String[] params) {
        try {
            InputStream input;

            URL url = new URL(URL + MANIFEST);
            URLConnection connection = url.openConnection();
            connection.connect();
            // download the file
            input = new BufferedInputStream(url.openStream());

            OutputStream output = mContext.openFileOutput(
                    MANIFEST, Context.MODE_PRIVATE);

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
        mLoadingDialog.cancel();
        mResponse.processFinish(result);
    }
}
