package com.ota.updates.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.UploadItem;
import com.ota.updates.items.VersionItem;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;

import in.uncod.android.bypass.Bypass;

public class AvailableFragment extends Fragment implements Constants {
    private Context mContext;

    private FragmentInteractionListener mListener;

    public AvailableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        mContext = activity;

        setupUpdateIconsText(view);

        setupChangelog(view, activity);

        return view;
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupUpdateIconsText(View view) {
        VersionSQLiteHelper versionSQLiteHelper = new VersionSQLiteHelper(mContext);
        VersionItem version = versionSQLiteHelper.getLastVersionItem();

        UploadSQLiteHelper uploadHelper = new UploadSQLiteHelper(mContext);
        int fullId = version.getFullUploadId();
        UploadItem uploadItem = uploadHelper.getUpload(fullId);

        View fileNameView = view.findViewById(R.id.filename);
        if (fileNameView != null) {
            TextView fileNameTV = (TextView) fileNameView;
            String text = version.getFullName();
            fileNameTV.setText(text);
        }

        View fileSizeView = view.findViewById(R.id.filesize);
        if (fileSizeView != null) {
            TextView fileSizeTV = (TextView) fileSizeView;
            String size = Utils.formatDataFromBytes(uploadItem.getSize());
            fileSizeTV.setText(size);
        }

        View fileHostView = view.findViewById(R.id.filehost);
        if (fileHostView != null) {
            TextView fileHostTV = (TextView) fileHostView;
            String url = uploadItem.getDownloadLink();
            String host = Utils.getUrlHost(url);
            fileHostTV.setText(host);
        }

        View fileHashView = view.findViewById(R.id.filehash);
        if (fileHashView != null) {
            TextView fileHashTV = (TextView) fileHashView;

            String text = uploadItem.getMd5();
            fileHashTV.setText(text);
        }
    }

    private void setupChangelog(View view, Context context) {
        View changelog = view.findViewById(R.id.changelog);
        if (changelog != null) {
            TextView tv = (TextView) changelog;
            Bypass byPass = new Bypass(context);
            VersionSQLiteHelper helper = new VersionSQLiteHelper(mContext);
            VersionItem versionItem = helper.getLastVersionItem();
            String changelogStr = versionItem.getChangelog();
            CharSequence string = byPass.markdownToSpannable(changelogStr);
            tv.setText(string);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
}
