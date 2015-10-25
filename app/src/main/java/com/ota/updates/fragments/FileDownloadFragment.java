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
import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.items.UploadItem;
import com.ota.updates.items.VersionItem;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;

import in.uncod.android.bypass.Bypass;

public class FileDownloadFragment extends Fragment implements Constants {
    public static final String FILE_ID = "fileId";
    public static final String FILE_TYPE = "fileType";
    private String TAG = this.getClass().getName();
    private AppCompatActivity mActivity;
    private FragmentInteractionListener mListener;
    private int mFileType;
    private int mFileId;

    public FileDownloadFragment() {
        // Required empty public constructor
    }

    public static FileDownloadFragment newInstance(int FileType, int fileId) {
        FileDownloadFragment fragment = new FileDownloadFragment();
        Bundle args = new Bundle();
        args.putInt(FILE_TYPE, FileType);
        args.putInt(FILE_ID, fileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFileType = getArguments().getInt(FILE_TYPE, -1);
            mFileId = getArguments().getInt(FILE_ID, -1);

            if (DEBUGGING) {
                Log.d(TAG, "File ID = " + mFileId);
                Log.d(TAG, "File Type ID" + mFileType);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_download, container, false);

        mActivity = (AppCompatActivity) getActivity();

        if (DEBUGGING) {
            Log.d(TAG, "File ID = " + mFileId);
            Log.d(TAG, "File Type ID = " + mFileType);
        }

        if (mFileType >= 0 && mFileId >= 0) {
            if (FILE_TYPE_VERSION == mFileType) {
                setupVersionFile(view, mFileId);
            } else if (FILE_TYPE_ADDON == mFileType) {
                setupAddonFile(view, mFileId);
            } else {
                setupErrorView(view);
                Log.e(TAG, "Error loading file with the id: " + mFileId + " and of the typeId " + mFileType);
            }
        }

        return view;
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupVersionFile(View view, int fileId) {
        VersionSQLiteHelper versionSQLiteHelper = new VersionSQLiteHelper(mActivity);
        VersionItem versionItem = versionSQLiteHelper.getVersion(fileId);

        TextView headline = (TextView) view.findViewById(R.id.headline);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);

        headline.setText(getResources().getString(R.string.file_information));
        subtitle.setText(getResources().getString(R.string.changelog));

        UploadSQLiteHelper uploadHelper = new UploadSQLiteHelper(mActivity);
        int fullId = versionItem.getFullUploadId();
        UploadItem uploadItem = uploadHelper.getUpload(fullId);

        View fileNameView = view.findViewById(R.id.filename);
        if (fileNameView != null) {
            TextView fileNameTV = (TextView) fileNameView;
            String text = versionItem.getFullName();
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

        View changelog = view.findViewById(R.id.changelog_or_description);
        if (changelog != null) {
            TextView tv = (TextView) changelog;
            Bypass byPass = new Bypass(mActivity);
            String changelogStr = versionItem.getChangelog();
            CharSequence string = byPass.markdownToSpannable(changelogStr);
            tv.setText(string);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupAddonFile(View view, int fileId) {
        AddonSQLiteHelper addonSQLiteHelper = new AddonSQLiteHelper(mActivity);
        AddonItem addonItem = addonSQLiteHelper.getAddon(fileId);

        TextView headline = (TextView) view.findViewById(R.id.headline);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);

        headline.setText(getResources().getString(R.string.file_information));
        subtitle.setText(getResources().getString(R.string.description));

        View fileNameView = view.findViewById(R.id.filename);
        if (fileNameView != null) {
            TextView fileNameTV = (TextView) fileNameView;
            String text = addonItem.getName();
            fileNameTV.setText(text);
        }

        View fileSizeView = view.findViewById(R.id.filesize);
        if (fileSizeView != null) {
            TextView fileSizeTV = (TextView) fileSizeView;
            String size = Utils.formatDataFromBytes(addonItem.getSize());
            fileSizeTV.setText(size);
        }

        View fileHostView = view.findViewById(R.id.filehost);
        if (fileHostView != null) {
            TextView fileHostTV = (TextView) fileHostView;
            String url = addonItem.getDownloadLink();
            String host = Utils.getUrlHost(url);
            fileHostTV.setText(host);
        }

        View fileHashView = view.findViewById(R.id.filehash);
        if (fileHashView != null) {
            TextView fileHashTV = (TextView) fileHashView;

            String text = addonItem.getMd5();
            fileHashTV.setText(text);
        }

        View changelog = view.findViewById(R.id.changelog_or_description);
        if (changelog != null) {
            TextView tv = (TextView) changelog;
            Bypass byPass = new Bypass(mActivity);
            String changelogStr = addonItem.getDescription();
            CharSequence string = byPass.markdownToSpannable(changelogStr);
            tv.setText(string);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupErrorView(View view) {
        TextView headline = (TextView) view.findViewById(R.id.headline);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);

        String error = getResources().getString(R.string.error);

        headline.setText(getResources().getString(R.string.error));
        subtitle.setText(getResources().getString(R.string.error));

        View fileNameView = view.findViewById(R.id.filename);
        if (fileNameView != null) {
            TextView fileNameTV = (TextView) fileNameView;
            fileNameTV.setText(error);
        }

        View fileSizeView = view.findViewById(R.id.filesize);
        if (fileSizeView != null) {
            TextView fileSizeTV = (TextView) fileSizeView;
            fileSizeTV.setText(error);
        }

        View fileHostView = view.findViewById(R.id.filehost);
        if (fileHostView != null) {
            TextView fileHostTV = (TextView) fileHostView;
            fileHostTV.setText(error);
        }

        View fileHashView = view.findViewById(R.id.filehash);
        if (fileHashView != null) {
            TextView fileHashTV = (TextView) fileHashView;
            fileHashTV.setText(error);
        }

        View changelog = view.findViewById(R.id.changelog_or_description);
        if (changelog != null) {
            TextView tv = (TextView) changelog;
            tv.setText(error);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
        }
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
}
