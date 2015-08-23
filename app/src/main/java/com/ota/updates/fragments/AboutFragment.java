package com.ota.updates.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.tasks.AsyncResponse;
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
    private Context mContext;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance(String param1, String param2) {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);

        setupAppVersion(view);

        new ChangelogAsyncTask(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                if (DEBUGGING) {
                    Log.d(TAG, "App changelog file finished downloading properly");
                }
                File changelogFile = new File(mContext.getApplicationContext().getFilesDir(), CHANGELOG);
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
            Log.d(TAG, "Setting up changelog");
        }
        TextView changelogTV = (TextView) view.findViewById(R.id.changelog);
        Bypass bypass = new Bypass(mContext);
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
        try {
            mListener = (FragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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