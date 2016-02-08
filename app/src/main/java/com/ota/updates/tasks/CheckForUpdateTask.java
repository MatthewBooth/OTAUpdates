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

import com.ota.updates.callbacks.AsyncResponse;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.utils.constants.App;
import com.ota.updates.utils.Utils;

public class CheckForUpdateTask extends AsyncTask<String, Integer, Boolean> implements App {
    public final String TAG = this.getClass().getSimpleName();
    public AsyncResponse mResponse;
    private Context mContext;

    public CheckForUpdateTask(Context context, AsyncResponse response) {
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


