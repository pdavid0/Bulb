package com.pdavid.android.widget.bulb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.geo.SimpleGeofence;

import java.util.List;

/**
 * @author pdavid
 */
public class SimpleGeofenceAdapter extends ArrayAdapter<SimpleGeofence> {
    private final LayoutInflater inflater;
    private final List<SimpleGeofence> object;

    public SimpleGeofenceAdapter(Context context, List<SimpleGeofence> objects) {
        super(context, R.layout.list_item_simple_geofence, objects);
        this.inflater = LayoutInflater.from(context);
        this.object=objects;
    }

    @Override
    public int getCount() {
        return object.size();
    }

    @Override
    public SimpleGeofence getItem(int position) {
        return object.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_simple_geofence, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.Title)).setText(getItem(position).getId());
        return convertView;
    }
}
