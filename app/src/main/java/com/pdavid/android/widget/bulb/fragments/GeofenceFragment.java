package com.pdavid.android.widget.bulb.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ListView;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.activities.GeofenceCreationDialogActivity;
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
    @InjectView(R.id.listView)
    ListView mListView;
    @InjectView(R.id.fragment_main_add_geofence_btn)
    Button mNewGeofenceBtn;
    private SimpleGeofenceAdapter geofenceAdapter;
    private ArrayList<SimpleGeofence> mGeofenceList;

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
        mStore = new SimpleGeofenceStore(getActivity());

        mGeofenceList = new ArrayList<SimpleGeofence>();
        mGeofenceList.addAll(Persistence.getInstance(getActivity()).getStore().list());
        geofenceAdapter = new SimpleGeofenceAdapter(getActivity(), mGeofenceList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddGeofenceBtn.animate().translationY(100);
        int height = getView().getLayoutParams().height / 2;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        height = size.y / 4;

        Log.w("AnimationInfo", "height:" + height);
        mNewGeofenceBtn
                .animate()
                .setDuration(android.R.integer.config_longAnimTime * 2)
                .yBy(100)
                .scaleX(2F)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.fragment_main_add_geofence_btn)
    public void showGeofenceDialogCreation() {
        getActivity().startActivityForResult(new Intent(getActivity(), GeofenceCreationDialogActivity.class), Activity.RESULT_OK);
    }

    public void refresh() {
        mGeofenceList.addAll(Persistence.getInstance(getActivity()).getStore().list());
    }
}
