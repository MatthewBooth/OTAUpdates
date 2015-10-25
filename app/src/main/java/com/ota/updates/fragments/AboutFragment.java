package com.ota.updates.fragments;
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.callbacks.AsyncResponse;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;

import java.io.File;
import java.io.IOException;

import in.uncod.android.bypass.Bypass;

public class AboutFragment extends Fragment implements Constants {

    private static final String CHANGELOG = "Changelog.md";
    private String TAG = this.getClass().getName();
    private FragmentInteractionListener mListener;
    private AppCompatActivity mActivity;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);

        setupAppVersion(view);

        new ChangelogAsyncTask(mActivity, new AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                if (DEBUGGING) {
                    Log.d(TAG, "App changelog_or_description file finished downloading properly");
                }
                File changelogFile = new File(mActivity.getApplicationContext().getFilesDir(), CHANGELOG);
                setupChangelog(view, changelogFile);
            }
        }).execute();

        return view;
    }

    private void setupAppVersion(View view) {
        TextView tv = (TextView) view.findViewById(R.id.app_version);
        String text = getResources().getString(R.string.about_version_number) + " " + getResources().getString(R.string.app_version);
        tv.setText(text);
    }

    private void setupChangelog(View view, File file) {
        if (DEBUGGING) {
            Log.d(TAG, "Setting up changelog_or_description");
        }
        TextView changelogTV = (TextView) view.findViewById(R.id.changelog_or_description);
        Bypass bypass = new Bypass(mActivity);
        String changelogString;
        try {
            changelogString = Utils.getFileContents(file);

        } catch (IOException e) {
            changelogString = getResources().getString(R.string.changelog_error);
            e.printStackTrace();
        }
        CharSequence changelogText = bypass.markdownToSpannable(changelogString);
        changelogTV.setText(changelogText);
        changelogTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (FragmentInteractionListener) mActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(mActivity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mListener = null;
    }

    public class ChangelogAsyncTask extends AsyncTask<Void, Void, Boolean> {
        public final String TAG = this.getClass().getSimpleName();
        private final String mUrl;
        private AsyncResponse mResponse;
        private Context mContext;

        public ChangelogAsyncTask(Context context, AsyncResponse response) {
            mContext = context;
            mResponse = response;
            mUrl = context.getResources().getString(R.string.changelog_url);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Utils.downloadFile(mContext, mUrl, CHANGELOG);
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
}