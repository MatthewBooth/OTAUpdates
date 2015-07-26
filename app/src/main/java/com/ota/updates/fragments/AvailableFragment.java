package com.ota.updates.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;
import com.ota.updates.R;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableFragment extends Fragment implements Constants {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentInteractionListener mListener;

    public AvailableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableFragment newInstance(String param1, String param2) {
        AvailableFragment fragment = new AvailableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        setupToolbar(view, activity);

        setupUpdateIcons(view);

        setupUpdateIconsText(view);

        return view;
    }

    /**
     * Sets up the toolbar for the fragment
     *
     * @param view     The root view for the fragment
     * @param activity The activity that loads the fragment
     */
    private void setupToolbar(View view, AppCompatActivity activity) {
        View toolbarView = view.findViewById(R.id.toolbar);
        if (toolbarView != null) {
            Toolbar toolbar = (Toolbar) toolbarView;
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            Utils.setupDrawer(activity, activity, toolbar, UPDATE_CHECK_ACTIVITY);
        }
    }

    /**
     * Sets up the update icon text for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupUpdateIconsText(View view) {
        View fileNameView = view.findViewById(R.id.filename);
        if (fileNameView != null) {
            TextView fileNameTV = (TextView) fileNameView;
        }

        View fileSizeView = view.findViewById(R.id.filesize);
        if (fileSizeView != null) {
            TextView fileSizeTV = (TextView) fileSizeView;
        }

        View fileHostView = view.findViewById(R.id.filehost);
        if (fileHostView != null) {
            TextView fileHostTV = (TextView) fileHostView;
        }

        View fileHashView = view.findViewById(R.id.filehash);
        if (fileHashView != null) {
            TextView fileHashTV = (TextView) fileHashView;
        }
    }

    /**
     * Sets up the update icons for the upper seam of the app
     *
     * @param view The root view for the fragment
     */
    private void setupUpdateIcons(View view) {
        View fileNameIconView = view.findViewById(R.id.filename_icon);
        if (fileNameIconView != null) {
            TextView iconTV = (TextView) fileNameIconView;
            Iconify.setIcon(iconTV, Iconify.IconValue.fa_file_archive_o);
        }

        View fileSizeIconView = view.findViewById(R.id.filesize_icon);
        if (fileSizeIconView != null) {
            TextView iconTV = (TextView) fileSizeIconView;
            Iconify.setIcon(iconTV, Iconify.IconValue.fa_arrows_h);
        }

        View fileHostIconView = view.findViewById(R.id.filehost_icon);
        if (fileHostIconView != null) {
            TextView iconTV = (TextView) fileHostIconView;
            Iconify.setIcon(iconTV, Iconify.IconValue.fa_globe);
        }

        View fileHashIconView = view.findViewById(R.id.filehash_icon);
        if (fileHashIconView != null) {
            TextView iconTV = (TextView) fileHashIconView;
            Iconify.setIcon(iconTV, Iconify.IconValue.fa_check_circle_o);
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
