package com.ota.updates.tasks;
/*
 * Copyright (C) 2015 Matt Booth.
 *
 * Licensed under the Attribution-NonCommercial-ShareAlike 4.0 International
 * (the "License") you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ota.updates.callbacks.AsyncResponse;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.Utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadJsonTask extends AsyncTask<String, Integer, Boolean> implements Constants {
    public final String TAG = this.getClass().getSimpleName();
    public AsyncResponse mResponse;
    private Context mContext;

    private String mUrl;
    private String mManifestFilename;

    public DownloadJsonTask(Context context, AsyncResponse response) {
        mContext = context;
        mResponse = response;
        mUrl = Utils.getProp(PROP_MANIFEST);
        mManifestFilename = Utils.getManifestFilename();
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
