package com.pdavid.android.widget.bulb.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.adapters.LightsAdapter;
import com.pdavid.android.widget.bulb.utils.persistance.Persistence;

import java.util.Collection;
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

    @InjectView(R.id.lfxbulb_grid)
    GridView mLfxbulbGrid;
    private LightsAdapter adapter;
    private LFXNetworkContext localNetworkContext;
    private LFXLightCollection mAllLights;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localNetworkContext = LFXClient.getSharedInstance(getActivity()).getLocalNetworkContext();
        localNetworkContext.connect();
        mAllLights = localNetworkContext.getAllLightsCollection();
        mAllLights.getLights();
        localNetworkContext.scanNetworkForLightStates();

        localNetworkContext.addNetworkContextListener(new LFXNetworkContext.LFXNetworkContextListener() {
            @Override
            public void networkContextDidConnect(LFXNetworkContext networkContext) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void networkContextDidDisconnect(LFXNetworkContext networkContext) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void networkContextDidAddTaggedLightCollection(LFXNetworkContext networkContext, LFXTaggedLightCollection collection) {

            }

            @Override
            public void networkContextDidRemoveTaggedLightCollection(LFXNetworkContext networkContext, LFXTaggedLightCollection collection) {

            }
        });

        mAllLights.getLights();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<LFXLight> mAllLightsLights = mAllLights.getLights();
        if (mAllLightsLights.size() > 0) {
            Persistence.getInstance(getActivity()).getLFXBulbStore().storeAll(mAllLightsLights);
        } else {
            mAllLightsLights.addAll(Persistence.getInstance(getActivity()).getLFXBulbStore().list());
        }
        adapter = new LightsAdapter(getActivity(), mAllLightsLights);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lfxbulbs, container, false);
        ButterKnife.inject(this, view);
        mLfxbulbGrid.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLfxbulbGrid.setAdapter(this.adapter);
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
