package com.pdavid.android.widget.bulb.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
public class LightsAdapter extends BaseAdapter {
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
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.bulb_widget_list_item, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LFXLight item = (LFXLight) getItem(position);
        holder.mBulbListItemTitle.setText(item.getLabel());

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

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file R.layout.bulb_widget_list_item
     * for easy to all layout elements.
     *
     * @author Android Butter Zelezny, plugin for IntelliJ IDEA/Android Studio by Inmite (www.inmite.eu)
     */
    static class ViewHolder {
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

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
