package com.pdavid.android.widget.bulb.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pdavid.android.widget.bulb.R;

/**
 * Created by Philippe on 2014-07-26.
 */
public class GeofenceCreationDialog extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Create a new instance of GeofenceCreationDialog, providing "num"
     * as an argument.
     */
    public static GeofenceCreationDialog newInstance(int num) {
        GeofenceCreationDialog f = new GeofenceCreationDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_create_geo, container, false);
        return rootView;
    }
}
