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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.db.helpers.RomSQLiteHelper;
import com.ota.updates.items.RomItem;
import com.ota.updates.utils.FragmentInteractionListener;

import in.uncod.android.bypass.Bypass;

public class InfoFragment extends Fragment {

    private FragmentInteractionListener mListener;
    private AppCompatActivity mActivity;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView nameTv = (TextView) view.findViewById(R.id.name);
        TextView downloadsTv = (TextView) view.findViewById(R.id.downloads);
        TextView descriptionTv = (TextView) view.findViewById(R.id.changelog_or_description);

        RomSQLiteHelper helper = RomSQLiteHelper.getInstance(mActivity);
        RomItem romItem = helper.getRom();

        nameTv.setText(romItem.getName());

        String downloadsStr = getResources().getString(R.string.downloads);
        downloadsTv.setText(downloadsStr + ": " + romItem.getDownloads());

        Bypass bypass = new Bypass(mActivity);
        String description = romItem.getDescription();


        CharSequence descriptionText = bypass.markdownToSpannable(description);
        descriptionTv.setText(descriptionText);
        descriptionTv.setMovementMethod(LinkMovementMethod.getInstance());

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
}
