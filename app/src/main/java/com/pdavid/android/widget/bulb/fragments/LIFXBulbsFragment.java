package com.pdavid.android.widget.bulb.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.adapters.LightsAdapter;
import com.pdavid.android.widget.bulb.utils.persistance.LFXBulbStore;
import com.pdavid.android.widget.bulb.utils.persistance.Persistence;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lifx.java.android.client.LFXClient;
import lifx.java.android.light.LFXLight;
import lifx.java.android.light.LFXLightCollection;
import lifx.java.android.light.LFXTaggedLightCollection;
import lifx.java.android.network_context.LFXNetworkContext;

/**
 * @author pdavid
 */
public class LIFXBulbsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    //    @InjectView(R.id.lfxbulb_grid)
//    GridView mLfxbulbGrid;
    @InjectView(R.id.lfxbulb_grid)
    RecyclerView mLfxbulbGrid;
    private LightsAdapter adapter;
    private LFXNetworkContext localNetworkContext;
    private LFXLightCollection mAllLights;
    private LinearLayoutManager mLayoutManager;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LIFXBulbsFragment newInstance(int sectionNumber) {
        LIFXBulbsFragment fragment = new LIFXBulbsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        localNetworkContext = LFXClient.getSharedInstance(getActivity()).getLocalNetworkContext();
        localNetworkContext.connect();
        mAllLights = localNetworkContext.getAllLightsCollection();
//        mAllLights.getLights();
        localNetworkContext.scanNetworkForLightStates();

        localNetworkContext.addNetworkContextListener(new LFXNetworkContext.LFXNetworkContextListener() {
            @Override
            public void networkContextDidConnect(LFXNetworkContext networkContext) {
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), String.format("%s has connected", networkContext.getName()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkContextDidDisconnect(LFXNetworkContext networkContext) {
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Network disconnected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkContextDidAddTaggedLightCollection(LFXNetworkContext networkContext, LFXTaggedLightCollection collection) {
                Toast.makeText(getActivity(), String.format("%d tagged lights", collection.getLights().size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkContextDidRemoveTaggedLightCollection(LFXNetworkContext networkContext, LFXTaggedLightCollection collection) {
                Toast.makeText(getActivity(), String.format("%d tagged lights removed", collection.getLights().size()), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<LFXLight> lights = mAllLights.getLights();
        Toast.makeText(getActivity(), String.format("%d lights", lights.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<LFXLight> mAllLightsLights = mAllLights.getLights();
        Persistence instance = Persistence.getInstance(getActivity());
        LFXBulbStore lfxBulbStore = instance.getLFXBulbStore();

        if (mAllLightsLights.size() > 0) {
            lfxBulbStore.storeAll(mAllLightsLights);
        } else {
            mAllLightsLights.addAll(lfxBulbStore.list());
        }
        Toast.makeText(getActivity(), String.format("%d lights", mAllLightsLights.size()), Toast.LENGTH_SHORT).show();
        adapter = new LightsAdapter(getActivity(), mAllLightsLights);
        if (mLfxbulbGrid != null) {
            mLfxbulbGrid.setAdapter(adapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lfxbulbs, container, false);
        ButterKnife.inject(this, view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mLfxbulbGrid.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLfxbulbGrid.setLayoutManager(mLayoutManager);
        mLfxbulbGrid.setAdapter(adapter);
//        mLfxbulbGrid.setOnItemClickListener(this);
//        mLfxbulbGrid.setEmptyView(view.findViewById(R.id.lfxbulb_grid_empty_view));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mLfxbulbGrid.setAdapter(this.adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
