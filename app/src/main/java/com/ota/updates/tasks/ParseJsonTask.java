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
import com.ota.updates.json.AddonJsonParser;
import com.ota.updates.json.RomJsonParser;
import com.ota.updates.json.VersionJsonParser;
import com.ota.updates.utils.constants.App;
import com.ota.updates.utils.Utils;

import java.io.File;
import java.io.IOException;

public class ParseJsonTask extends AsyncTask<String, Integer, Boolean> implements App {

    public final String TAG = this.getClass().getSimpleName();
    public AsyncResponse mResponse;
    private Context mContext;

    public ParseJsonTask(Context context, AsyncResponse response) {
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
            if (DEBUGGING) {
                Log.d(TAG, json);
            }
            // Parse and populate our Database
            boolean romParsed = new RomJsonParser(mContext, json).parse();
            boolean versionParsed = new VersionJsonParser(mContext, json).parse();
            boolean addonParsed = new AddonJsonParser(mContext, json).parse();

            if (DEBUGGING) {
                Log.d(TAG, "JSON data parsed status = " + (versionParsed && addonParsed && romParsed));
            }

            return versionParsed && addonParsed && romParsed;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResponse.processFinish(result);
    }
}
