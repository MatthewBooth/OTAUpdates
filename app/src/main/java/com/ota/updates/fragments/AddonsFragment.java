package com.ota.updates.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.uncod.android.bypass.Bypass;

public class AddonsFragment extends Fragment implements Constants {
    private FragmentInteractionListener mListener;
    private Context mContext;

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

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        mContext = activity;

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_versions, container, false);

        AddonSQLiteHelper addonSQLiteHelper = new AddonSQLiteHelper(mContext);

        ArrayList<AddonItem> addonsList = addonSQLiteHelper.getListOfAddons();

        if (!addonsList.isEmpty()) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(new RecyclerAdapter(addonsList));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (FragmentInteractionListener) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString()
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_addons_list_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // Addon Item at latest position in the list
            AddonItem item = mItems.get(position);

            // Title
            viewHolder.mTitle.setText(item.getName());

            // Description
            Bypass byPass = new Bypass(mContext);
            String descriptionStr = item.getDescription();
            CharSequence string = byPass.markdownToSpannable(descriptionStr);
            viewHolder.mDescription.setText(string);
            viewHolder.mDescription.setMovementMethod(LinkMovementMethod.getInstance());

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
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTitle;
            private final TextView mUpdatedOn;
            private final TextView mFilesize;
            private final TextView mDescription;
            private final Button mButtons;

            ViewHolder(View view) {
                super(view);
                mTitle = (TextView) view.findViewById(R.id.title);
                mUpdatedOn = (TextView) view.findViewById(R.id.updatedOn);
                mFilesize = (TextView) view.findViewById(R.id.size);
                mDescription = (TextView) view.findViewById(R.id.description);
                mButtons = (Button) view.findViewById(R.id.download_button);
            }
        }

    }
}
