package com.ota.updates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ota.updates.R;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DownloadManagerFragment extends Fragment implements App {
    private String TAG = this.getClass().getSimpleName();
    private AppCompatActivity mActivity;
    private FragmentInteractionListener mListener;

    private Boolean mDownloadsEmpty;

    public static DownloadManagerFragment newInstance() {
        DownloadManagerFragment fragment = new DownloadManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_download_manager, container, false);

        DownloadsSQLiteHelper downloadsSQLiteHelper = new DownloadsSQLiteHelper(mActivity);

        ArrayList<DownloadItem> listOfDownloads = downloadsSQLiteHelper.getListOfDownloads();

        mDownloadsEmpty = listOfDownloads.isEmpty();

        if (!mDownloadsEmpty) {
            setHasOptionsMenu(true);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.setAdapter(new RecyclerAdapter(listOfDownloads));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.download_manager_menu, menu);
        MenuItem item = menu.findItem(R.id.clear_all);
        item.setEnabled(mDownloadsEmpty);
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

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private ArrayList<DownloadItem> mItems;

        RecyclerAdapter(ArrayList<DownloadItem> items) {
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_download_list_items, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // Addon Item at latest position in the list
            final DownloadItem downloadItem = mItems.get(position);

            if (downloadItem.getDownloadType() == DOWNLOAD_TYPE_VERSION) {
                VersionSQLiteHelper versionSQLiteHelper = new VersionSQLiteHelper(mActivity);
                VersionItem item = versionSQLiteHelper.getVersion(downloadItem.getFileId());
                setupVersionItem(viewHolder, item, downloadItem);
            } else {
                AddonSQLiteHelper addonSQLiteHelper = new AddonSQLiteHelper(mActivity);
                AddonItem item = addonSQLiteHelper.getAddon(downloadItem.getFileId());
                setupAddonItem(viewHolder, item, downloadItem);
            }
        }

        private void setupAddonItem(ViewHolder viewHolder, AddonItem item, final DownloadItem downloadItem) {
            // Title
            viewHolder.mTitle.setText(item.getName());

            // Filesize
            int size = item.getSize();
            String formattedSize = Utils.formatDataFromBytes(size);
            viewHolder.mFilesize.setText(formattedSize);

            // Date
            String addedOnStr = getResources().getString(R.string.added_on);
            String finishedOnStr = getResources().getString(R.string.finished_on);
            String timeSuffix;

            if (downloadItem.getDownloadStatus() == DOWNLOAD_STATUS_RUNNING) {
                timeSuffix = addedOnStr;
            } else {
                timeSuffix = finishedOnStr;
            }

            String date = downloadItem.getDownloadStarted().toString();

            Locale locale = Locale.getDefault();
            DateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd", locale);
            DateFormat toDate = new SimpleDateFormat("dd, MMMM yyyy", locale);

            try {
                date = toDate.format(fromDate.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.mUpdatedOn.setText(timeSuffix + " " + date);

            viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.stopDownload(downloadItem.getFileId());
                }
            });
        }

        private void setupVersionItem(ViewHolder viewHolder, VersionItem item, final DownloadItem downloadItem) {
            // Title
            viewHolder.mTitle.setText(item.getFullName());

            // Filesize
            UploadSQLiteHelper uploadSQLiteHelper = new UploadSQLiteHelper(mActivity);
            int fullUploadId = item.getFullUploadId();
            UploadItem uploadItem = uploadSQLiteHelper.getUpload(fullUploadId);
            int size = uploadItem.getSize();
            String formattedSize = Utils.formatDataFromBytes(size);
            viewHolder.mFilesize.setText(formattedSize);

            // Date
            String addedOnStr = getResources().getString(R.string.added_on);
            String finishedOnStr = getResources().getString(R.string.finished_on);
            String date = downloadItem.getDownloadStarted().toString();

            Locale locale = Locale.getDefault();
            DateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd", locale);
            DateFormat toDate = new SimpleDateFormat("dd, MMMM yyyy", locale);

            try {
                date = toDate.format(fromDate.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.mUpdatedOn.setText(addedOnStr + " " + date);

            viewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.stopDownload(downloadItem.getFileId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTitle;
            private final TextView mUpdatedOn;
            private final TextView mFilesize;
            private final Button mDeleteButton;

            ViewHolder(View view) {
                super(view);
                mTitle = (TextView) view.findViewById(R.id.headline);
                mUpdatedOn = (TextView) view.findViewById(R.id.updated_on);
                mFilesize = (TextView) view.findViewById(R.id.size);
                mDeleteButton = (Button) view.findViewById(R.id.delete);
            }
        }
    }
}
