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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.utils.constants.App;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AddonsFragment extends Fragment implements App {
    private FragmentInteractionListener mListener;
    private AppCompatActivity mActivity;

    public AddonsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = (AppCompatActivity) getActivity();

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_versions, container, false);

        AddonSQLiteHelper addonSQLiteHelper = AddonSQLiteHelper.getInstance(mActivity);

        ArrayList<AddonItem> addonsList = addonSQLiteHelper.getListOfAddons();

        if (!addonsList.isEmpty()) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            recyclerView.setAdapter(new RecyclerAdapter(addonsList));
        }
        return view;
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

        private ArrayList<AddonItem> mItems;

        RecyclerAdapter(ArrayList<AddonItem> items) {
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_file_list_items, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // Addon Item at latest position in the list
            final AddonItem item = mItems.get(position);

            // Title
            viewHolder.mTitle.setText(item.getName());

            // Filesize
            String formattedSize = Utils.formatDataFromBytes(item.getSize());
            viewHolder.mFilesize.setText(formattedSize);

            // Date
            String updatedOnStr = getResources().getString(R.string.updated_on);
            String date = item.getPublishedAt();

            Locale locale = Locale.getDefault();
            DateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd", locale);
            DateFormat toDate = new SimpleDateFormat("dd, MMMM yyyy", locale);

            try {
                date = toDate.format(fromDate.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.mUpdatedOn.setText(updatedOnStr + " " + date);

            viewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onOpenFileDownloadView(FILE_TYPE_ADDON, item.getId());
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
            private final Button mButton;

            ViewHolder(View view) {
                super(view);
                mTitle = (TextView) view.findViewById(R.id.headline);
                mUpdatedOn = (TextView) view.findViewById(R.id.updated_on);
                mFilesize = (TextView) view.findViewById(R.id.size);
                mButton = (Button) view.findViewById(R.id.open);
            }
        }

    }
}
