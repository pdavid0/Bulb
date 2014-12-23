package com.pdavid.android.widget.bulb.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.activities.GeofenceCreationDialogActivity;
import com.pdavid.android.widget.bulb.activities.MainActivity;
import com.pdavid.android.widget.bulb.adapters.SimpleGeofenceAdapter;
import com.pdavid.android.widget.bulb.geo.SimpleGeofence;
import com.pdavid.android.widget.bulb.utils.persistance.Persistence;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Philippe on 2014-07-26.
 */
public class GeofenceFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    //    @InjectView(R.id.listView)
//    ListView mListView;
    @InjectView(R.id.fragment_main_add_geofence_btn)
    Button mNewGeofenceBtn;
    @InjectView(R.id.my_recycler_view)
    public RecyclerView mRecyclerView;

    private SimpleGeofenceAdapter mGeofenceAdapter;
    private ArrayList<SimpleGeofence> mGeofenceList;
    private LinearLayoutManager mLayoutManager;

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
        setRetainInstance(true);
        mGeofenceList = new ArrayList<>();
        mGeofenceList.addAll(Persistence.getInstance(getActivity()).getGeofenceStore().list());
        mGeofenceAdapter = new SimpleGeofenceAdapter(mGeofenceList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mGeofenceAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mGeofenceList.addAll(Persistence.getInstance(getActivity()).getGeofenceStore().list());
        mGeofenceAdapter.addAll(Persistence.getInstance(getActivity()).getGeofenceStore().list());
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.fragment_main_add_geofence_btn)
    public void showGeofenceDialogCreation() {
        ((MainActivity) getActivity()).startGeofenceCreationDialog(new Intent(getActivity(), GeofenceCreationDialogActivity.class), 100);
    }
}
