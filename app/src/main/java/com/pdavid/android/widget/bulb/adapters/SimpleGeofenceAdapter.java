package com.pdavid.android.widget.bulb.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.geo.SimpleGeofence;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author pdavid
 */
public class SimpleGeofenceAdapter extends RecyclerView.Adapter<SimpleGeofenceAdapter.ViewHolder> {
    private List<SimpleGeofence> object;

    public void addAll(List<SimpleGeofence> list) {
        object = list;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final GoogleMap mMap;
        private Circle circle;
        @InjectView(R.id.Title)
        public TextView mTitle;
        @InjectView(R.id.type)
        public TextView mType;

        public ViewHolder(Context c, View v) {
            super(v);
            ButterKnife.inject(this, v);
            // wtf  ...
            mMap = ((MapFragment) ((Activity) c).getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }

    public SimpleGeofenceAdapter(List<SimpleGeofence> objects) {
        this.object = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View convertView = LayoutInflater.from(context)
                .inflate(R.layout.list_item_simple_geofence, parent, false);

        ViewHolder vh = new ViewHolder(context, convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleGeofence simpleGeofence = object.get(position);
        holder.mTitle.setText(simpleGeofence.getId());
        int transitionType = simpleGeofence.getTransitionType();
        holder.mType.setText(transitionType == 3 ? "Both" : transitionType == 2 ? "Exit" : "Enter");

        LatLng latLng = new LatLng(
                simpleGeofence.getLatitude(),
                simpleGeofence.getLongitude());
        holder.mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, 16));
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .fillColor(Color.argb(127, 139, 195, 74))
                .radius(simpleGeofence.getRadius()); // In meters
        holder.mMap.addCircle(circleOptions);
    }

    @Override
    public int getItemCount() {
        return object.size();
    }
}
