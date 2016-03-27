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

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.callbacks.DownloadProgressCallback;
import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.db.helpers.DownloadsSQLiteHelper;
import com.ota.updates.db.helpers.UploadSQLiteHelper;
import com.ota.updates.db.helpers.VersionSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.items.DownloadItem;
import com.ota.updates.items.UploadItem;
import com.ota.updates.items.VersionItem;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;
import com.ota.updates.utils.constants.App;
import com.ota.updates.utils.fontdrawing.MaterialIconsDrawable;

import in.uncod.android.bypass.Bypass;

public class FileViewerFragment extends Fragment implements App {

    private static String TAG = FileViewerFragment.class.getSimpleName();

    private static FileViewerFragment mInstance =  null;

    public static final String FILE_ID = "fileId";
    public static final String FILE_TYPE = "fileType";

    private AppCompatActivity mActivity;
    private FragmentInteractionListener mListener;
    private int mFileType;
    private int mFileId;

    private Long mDownloadId;

    private DownloadsSQLiteHelper mDownloadsSQLiteHelper;

    private Boolean mDownloadInProgress;
    private Boolean mMonitorProgress;

    private DownloadManager mDownloadManager;

    private ProgressBar mProgressBar;

    public FileViewerFragment() {
        // Required empty public constructor
    }

    public static FileViewerFragment getInstance(int FileType, int fileId) {

        if (mInstance != null && mInstance.mFileId == fileId) {
            if (DEBUGGING) {
                Log.d(TAG, "Existing instance of fragment returned");
            }
            return mInstance;
        } else {
           mInstance = new FileViewerFragment();
            Bundle args = new Bundle();
            args.putInt(FILE_TYPE, FileType);
            args.putInt(FILE_ID, fileId);
            mInstance.setArguments(args);

            if (DEBUGGING) {
                Log.d(TAG, "New instance of fragment returned");
            }

            return mInstance;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) getActivity();

        if (getArguments() != null) {
            mFileType = getArguments().getInt(FILE_TYPE, -1);
            mFileId = getArguments().getInt(FILE_ID, -1);

            if (DEBUGGING) {
                Log.d(TAG, "File ID = " + mFileId);
                Log.d(TAG, "File Type ID = " + mFileType);
            }
        }

        // Setup the DownloadManager
        mDownloadManager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);

        // Initial state of the download. Assume it's not ongoing
        mDownloadInProgress = false;

        // Get the DB helper
        mDownloadsSQLiteHelper = DownloadsSQLiteHelper.getInstance(mActivity);

        // Check to see if the download is ongoing currently
        DownloadItem downloadItem = mDownloadsSQLiteHelper.getDownloadEntryByFileId(mFileId);
        if (downloadItem != null) {
            if (downloadItem.getDownloadStatus().equals(DOWNLOAD_STATUS_RUNNING)) {
                mDownloadInProgress = true;
                mDownloadId = downloadItem.getDownloadId();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_viewer, container, false);

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

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

        return view;
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupVersionFile(View view, int fileId) {
        VersionSQLiteHelper versionSQLiteHelper = VersionSQLiteHelper.getInstance(mActivity);
        VersionItem versionItem = versionSQLiteHelper.getVersion(fileId);

        TextView headline = (TextView) view.findViewById(R.id.headline);
        TextView subtitle = (TextView) view.findViewById(R.id.subtitle);

        headline.setText(getResources().getString(R.string.file_information));
        subtitle.setText(getResources().getString(R.string.changelog));

        UploadSQLiteHelper uploadHelper = UploadSQLiteHelper.getInstance(mActivity);
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

        setupFloatingActionButton(view, uploadItem.getDownloadLink(), versionItem.getFullName(), versionItem.getId(), DOWNLOAD_TYPE_VERSION);
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupAddonFile(View view, int fileId) {
        AddonSQLiteHelper addonSQLiteHelper = AddonSQLiteHelper.getInstance(mActivity);
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

        setupFloatingActionButton(view, addonItem.getDownloadLink(), addonItem.getName(), addonItem.getId(), DOWNLOAD_TYPE_ADDON);
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

    private void setupFloatingActionButton(View view, final String url, final String fileName, final int fileId, final int type) {
        final View fabViewDownload = view.findViewById(R.id.fabDownload);
        final View fabViewCancel = view.findViewById(R.id.fabCancel);

        // Start Download button
        final FloatingActionButton fabDownload = (FloatingActionButton) fabViewDownload;
        MaterialIconsDrawable.Builder buildDownload = new MaterialIconsDrawable.Builder(mActivity, R.string.mc_file_download);
        buildDownload.setSize(32);
        buildDownload.setColor(Color.WHITE);
        fabDownload.setImageDrawable(buildDownload.build());
        fabDownload.setVisibility(mDownloadInProgress ? View.GONE : View.VISIBLE);

        // Cancel Download button
        final FloatingActionButton fabCancel = (FloatingActionButton) fabViewCancel;
        MaterialIconsDrawable.Builder buildCancel = new MaterialIconsDrawable.Builder(mActivity, R.string.mc_close);
        buildCancel.setSize(32);
        buildCancel.setColor(Color.WHITE);
        fabCancel.setImageDrawable(buildCancel.build());
        fabCancel.setVisibility(mDownloadInProgress ? View.VISIBLE : View.GONE);

        // Click listeners
        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(url, fileName, fileId, type, fabDownload, fabCancel);
            }
        });
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDownload(fileId, fabCancel, fabDownload);
            }
        });
    }

    private void stopDownload(int fileId, FloatingActionButton fabCancel, FloatingActionButton fabDownload) {
        mDownloadInProgress = false;
        mListener.stopDownload(fileId);
        fabCancel.setVisibility(View.GONE);
        fabDownload.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
    }

    private void startDownload(String url, String fileName, int fileId, int type, final FloatingActionButton fabDownload, final FloatingActionButton fabCancel) {
        mListener.startDownload(url, fileName, fileId, type, new DownloadProgressCallback() {
            @Override
            public void startMonitoring(Long output) {
                mDownloadId = output;
                startDownloadProgressMonitoring();
                mDownloadInProgress = true;
                fabDownload.setVisibility(View.GONE);
                fabCancel.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FILE_TYPE, mFileType);
        outState.putInt(FILE_ID, mFileId);
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
        mMonitorProgress = true;

        if (mDownloadInProgress) {
            startDownloadProgressMonitoring();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mListener = null;
        mMonitorProgress = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMonitorProgress = true;
        if (mDownloadInProgress) {
            startDownloadProgressMonitoring();
        }
    }

    private void startDownloadProgressMonitoring() {
        new DownloadProgress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMonitorProgress = false;
    }

    private class DownloadProgress extends AsyncTask<Long, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Long... params) {
            int previousValue = 0;
            while(mDownloadInProgress && mMonitorProgress) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(mDownloadId);

                Cursor cursor = mDownloadManager.query(q);
                cursor.moveToFirst();
                try {
                    final int bytesDownloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    final int bytesInTotal = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    final int progressPercent = (int) ((bytesDownloaded * 100l) / bytesInTotal);

                    if (progressPercent != previousValue) {
                        // Only publish every 1%, to reduce the amount of work being done.
                        publishProgress(progressPercent, bytesDownloaded, bytesInTotal);
                        previousValue = progressPercent;
                    }
                } catch (CursorIndexOutOfBoundsException | ArithmeticException e) {
                    Log.e(TAG, e.getMessage());
                }
                cursor.close();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            if (DEBUGGING) {
                Log.d(TAG, "Updating Progress - " + progress[0] + "%");
            }
            mProgressBar.setProgress(progress[0]);
        }
    }
}