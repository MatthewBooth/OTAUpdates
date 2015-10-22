package com.ota.updates.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Preferences;

public class CheckFragment extends Fragment implements Constants {
    private FragmentInteractionListener mListener;
    private Context mContext;

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

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        mContext = activity;

        TextView time = (TextView) view.findViewById(R.id.subtitle);
        String lastChecked = getResources().getString(R.string.last_checked_for_update);
        String timeChecked = Preferences.getUpdateLastChecked(mContext);
        time.setText(lastChecked + " " + timeChecked);

        View fabView = view.findViewById(R.id.fab);
        if (fabView != null) {
            FloatingActionButton fab = (FloatingActionButton) fabView;

            // TODO: Don't need an action on this Snackbar thing.
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
