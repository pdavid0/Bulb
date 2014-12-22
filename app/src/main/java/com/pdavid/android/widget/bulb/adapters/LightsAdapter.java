package com.pdavid.android.widget.bulb.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdavid.android.widget.bulb.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lifx.java.android.entities.LFXHSBKColor;
import lifx.java.android.entities.LFXTypes;
import lifx.java.android.light.LFXLight;

/**
 * Created by pdavid on 7/12/14.
 */
public class LightsAdapter extends RecyclerView.Adapter<LightsAdapter.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private final int red;
    private final int green;
    private final Context context;
    private final List<LFXLight> list;

    public LightsAdapter(Context context, List<LFXLight> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(android.R.color.holo_red_dark);
        green = context.getResources().getColor(android.R.color.holo_green_dark);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View convertView = LayoutInflater.from(context)
                .inflate(R.layout.bulb_widget_list_item, parent, false);

        ViewHolder vh = new ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final LFXLight item = list.get(position);
        holder.mBulbListItemTitle.setText(item.getLabel());

        if (item.getLights() != null) {
            LFXHSBKColor lfxhsbkColor = item.getColor();
            float[] hsv = new float[]{item.getColor().getHue(), lfxhsbkColor.getSaturation(), item.getColor().getBrightness()};
            int color = Color.HSVToColor(hsv);
            holder.mBulbListItemColor.setBackgroundColor(color);
            holder.mBulbListItemColorId.setText("H: " + lfxhsbkColor.getHue() + " S: " + lfxhsbkColor.getSaturation() + " B: " + lfxhsbkColor.getBrightness());

            String mTagsLablel = "";
            ArrayList<String> tags = item.getTags();
            if (tags.size() > 0) {

                for (String t : tags) {
                    mTagsLablel += t + ", ";
                }
                mTagsLablel.substring(0, mTagsLablel.length() - 1);
                holder.mBulbListItemTag.setText(mTagsLablel);
            }

            holder.mBulbListItemId.setText(item.getDeviceID());
            holder.mBuldListItemPowerState.setBackgroundColor(
                    (item.getPowerState().name().equals(LFXTypes.LFXPowerState.OFF.name())) ? red : green);
        } else {
            holder.mBulbListItemId.setText("Null Device");
            holder.mBulbListItemTag.setText("Null tags");
            holder.mBuldListItemPowerState.setBackgroundColor(red);
        }
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.bulb_list_item_title)
        TextView mBulbListItemTitle;
        @InjectView(R.id.bulb_list_item_tag)
        TextView mBulbListItemTag;
        @InjectView(R.id.buld_list_item_power_state)
        ImageView mBuldListItemPowerState;
        @InjectView(R.id.bulb_list_item_color)
        ImageView mBulbListItemColor;
        @InjectView(R.id.bulb_list_item_color_id)
        TextView mBulbListItemColorId;
        @InjectView(R.id.bulb_list_item_id)
        TextView mBulbListItemId;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }
}
