package com.ota.updates.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ota.updates.R;
import com.ota.updates.db.helpers.AddonSQLiteHelper;
import com.ota.updates.items.AddonItem;
import com.ota.updates.utils.Constants;
import com.ota.updates.utils.FragmentInteractionListener;
import com.ota.updates.utils.Utils;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import in.uncod.android.bypass.Bypass;

public class AddonsFragment extends ListFragment implements Constants {
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
            final AddonsArrayAdapter adapter = new AddonsArrayAdapter(mContext, addonsList);
            setListAdapter(new SlideExpandableListAdapter(adapter, R.id.download_button, R.id.expandable));
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

    public class AddonsArrayAdapter extends ArrayAdapter<AddonItem> {

        public AddonsArrayAdapter(Context context, ArrayList<AddonItem> addons) {
            super(context, 0, addons);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AddonItem item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_addons_list_item, parent, false);
            }

            // Normal
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView updatedOn = (TextView) convertView.findViewById(R.id.updatedOn);
            TextView filesize = (TextView) convertView.findViewById(R.id.size);
            TextView description = (TextView) convertView.findViewById(R.id.description);

            // Title
            title.setText(item.getName());

            // Description
            Bypass byPass = new Bypass(mContext);
            String descriptionStr = item.getDescription();
            CharSequence string = byPass.markdownToSpannable(descriptionStr);
            description.setText(string);
            description.setMovementMethod(LinkMovementMethod.getInstance());

            // Date
            //String UpdatedOnStr = convertView.getResources().getString(R.string.addons_updated_on);
            String date = item.getPublishedAt();

            Locale locale = Locale.getDefault();
            DateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd", locale);
            DateFormat toDate = new SimpleDateFormat("dd, MMMM yyyy", locale);

            try {
                date = toDate.format(fromDate.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            updatedOn.setText("Updated On" + " " + date);

            // Filesize
            String formattedSize = Utils.formatDataFromBytes(item.getSize());
            filesize.setText(formattedSize);

            return convertView;
        }
    }
}
