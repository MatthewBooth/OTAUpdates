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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.utils.constants.App;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Preferences;
import com.ota.updates.utils.fontdrawing.MaterialIconsDrawable;

public class CheckFragment extends Fragment implements App {
    private FragmentInteractionListener mListener;
    private AppCompatActivity mActivity;

    public CheckFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_check, container, false);

        mActivity = (AppCompatActivity) getActivity();

        TextView time = (TextView) view.findViewById(R.id.subtitle);
        String lastChecked = getResources().getString(R.string.last_checked_for_update);
        String timeChecked = Preferences.getUpdateLastChecked(mActivity);
        time.setText(lastChecked + " " + timeChecked);

        View fabView = view.findViewById(R.id.fabDownload);
        if (fabView != null) {
            FloatingActionButton fab = (FloatingActionButton) fabView;

            MaterialIconsDrawable.Builder build = new MaterialIconsDrawable.Builder(mActivity, R.string.mc_refresh);
            build.setSize(24);
            fab.setImageDrawable(build.build());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRefreshClickInteraction();
                }
            });
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
}
