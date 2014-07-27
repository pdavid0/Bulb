package com.pdavid.android.widget.bulb.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.dialogs.GeofenceCreationDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Philippe on 2014-07-26.
 */
public class GeofenceFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    @InjectView(R.id.listView)
    ListView mListView;
    @InjectView(R.id.fragment_main_add_geofence_btn)
    Button mFragmentMainAddGeofenceBtn;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GeofenceFragment newInstance(int sectionNumber) {
        GeofenceFragment fragment = new GeofenceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GeofenceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.fragment_main_add_geofence_btn)
    public void showGeofenceDialogCreation() {
        GeofenceCreationDialog.newInstance(1).show(getChildFragmentManager(), GeofenceCreationDialog.class.getSimpleName());
    }
}
