package com.pdavid.android.widget.bulb.utils.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import lifx.java.android.light.LFXLight;

/**
 * @author pdavid
 */
public class LFXBulbStore extends AbstractStore<LFXLight> {

    // Keys for flattened geofences stored in SharedPreferences
    public static final String KEY_NAME =
            "com.pdavid.android.persistance.geofence.KEY_LATITUDE";
    public static final String KEY_ID =
            "com.pdavid.android.persistance.geofence.KEY_LONGITUDE";
    public static final String KEY_COLOR =
            "com.pdavid.android.persistance.geofence.KEY_RADIUS";
    public static final String KEY_GROUP =
            "com.pdavid.android.persistance.geofence.KEY_EXPIRATION_DURATION";

    /*
     * Invalid values, used to test geofence storage when
     * retrieving geofences
     */
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;

    // The name of the SharedPreferences
    protected static final String STORE_NAME =
            "BulbStore";

    protected LFXBulbStore(Context context) {
        super(context);
    }

    @Override
    protected String getStoreName() {
        return STORE_NAME;
    }

    @Override
    public LFXLight get(String id) {
         /*
         * Get the latitude for the geofence identified by id, or
         * INVALID_FLOAT_VALUE if it doesn't exist
         */
        String name = mPrefs.getString(
                getFieldKey(id, KEY_NAME), "");
        /*
         * Get the longitude for the geofence identified by id, or
         * INVALID_FLOAT_VALUE if it doesn't exist
         */
        String _id = mPrefs.getString(
                getFieldKey(id, KEY_ID), "");
        /*
         * Get the color for the geofence identified by id, or
         * INVALID_FLOAT_VALUE if it doesn't exist
         */
        String color = mPrefs.getString(
                getFieldKey(id, KEY_COLOR), "");

        if (name != "" && id != "" && color != "") {
            // Return a fake LIFXLight object
            return new LFXLight();//_id, name, id, color
        } else {
            // Otherwise, return null.
            return null;
        }
    }

    @Override
    public boolean store(LFXLight object) {
        String deviceID = object.getDeviceID();
        saveID(deviceID);
        return setLIFXBulb(deviceID, object);
    }

    /**
     * Get a SharedPreferences editor instance. Among other
     * things, SharedPreferences ensures that updates are atomic
     * and non-concurrent
     */
    @Override
    public boolean update(LFXLight object) {
        String deviceID = object.getDeviceID();

        saveID(deviceID);
        setLIFXBulb(deviceID, object);
        return true;
    }

    private boolean setLIFXBulb(String deviceID, LFXLight object) {
        saveID(deviceID);
        SharedPreferences.Editor editor = mPrefs.edit();
        // Write the Geofence values to SharedPreferences
        editor.putString(
                getFieldKey(deviceID, KEY_NAME), deviceID);
        editor.putString(
                getFieldKey(deviceID, KEY_ID), deviceID);
        editor.putString(
                getFieldKey(deviceID, KEY_COLOR),
                object.getColor().toString());
//        editor.putLong(
//                getFieldKey(id, KEY_GROUP),
//                object.getT());
//        editor.putInt(
//                getFieldKey(id, KEY_TRANSITION_TYPE),
//                geofence.getTransitionType());
        // Commit the changes
        return editor.commit();
    }

    @Override
    public boolean delete(String id) {
        /*
         * Remove a flattened geofence object from storage by
         * removing all of its keys
         */
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(getFieldKey(id, KEY_NAME));
        editor.remove(getFieldKey(id, KEY_GROUP));
        editor.remove(getFieldKey(id, KEY_COLOR));
//        editor.remove(getFieldKey(id,
//                KEY_EXPIRATION_DURATION));
//        editor.remove(getFieldKey(id, KEY_TRANSITION_TYPE));
        editor.apply();
        return true;
    }

}
