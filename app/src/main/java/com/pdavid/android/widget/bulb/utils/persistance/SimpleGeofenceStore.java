package com.pdavid.android.widget.bulb.utils.persistance;

/**
 * Created by pdavid on 7/22/14.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.pdavid.android.widget.bulb.geo.GeofenceUtils;
import com.pdavid.android.widget.bulb.geo.SimpleGeofence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Storage for geofence values, implemented in SharedPreferences.
 */
public class SimpleGeofenceStore extends AbstractStore<SimpleGeofence> {
    // Keys for flattened geofences stored in SharedPreferences
    public static final String KEY_LATITUDE =
            "com.pdavid.android.persistance.geofence.KEY_LATITUDE";
    public static final String KEY_LONGITUDE =
            "com.pdavid.android.persistance.geofence.KEY_LONGITUDE";
    public static final String KEY_RADIUS =
            "com.pdavid.android.persistance.geofence.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION =
            "com.pdavid.android.persistance.geofence.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE =
            "com.pdavid.android.persistance.geofence.KEY_TRANSITION_TYPE";

    /*
     * Invalid values, used to test geofence storage when
     * retrieving geofences
     */
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
    // The name of the SharedPreferences
    protected static final String GEO_STORE =
            "SharedPreferences";


    // Create the SharedPreferences storage with private access only
    public SimpleGeofenceStore(Context context) {
        super(context);
    }

    @Override
    protected String getStoreName() {
        return GEO_STORE;
    }

    @Override
    public SimpleGeofence get(String id) {
        return getGeofence(id);
    }

    @Override
    public boolean store(SimpleGeofence object) {
//        object.setId(createId());
        saveID(object.getId());
        return setGeofence(object.getId(), object);
    }

    @Override
    public boolean update(SimpleGeofence object) {
        saveID(object.getId());
        setGeofence(object.getId(),object);
        return true;
    }

    @Override
    public boolean delete(String id) {
        clearGeofence(id);
        return true;
    }

    /**
     * Returns a stored geofence by its id, or returns null
     * if it's not found.
     *
     * @param id The ID of a stored geofence
     * @return A geofence defined by its center and radius. See
     */
    private SimpleGeofence getGeofence(String id) {
            /*
             * Get the latitude for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
        double lat = mPrefs.getFloat(
                getFieldKey(id, KEY_LATITUDE),
                INVALID_FLOAT_VALUE);
            /*
             * Get the longitude for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
        double lng = mPrefs.getFloat(
                getFieldKey(id, KEY_LONGITUDE),
                INVALID_FLOAT_VALUE);
            /*
             * Get the radius for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
        float radius = mPrefs.getFloat(
                getFieldKey(id, KEY_RADIUS),
                INVALID_FLOAT_VALUE);
            /*
             * Get the expiration duration for the geofence identified
             * by id, or INVALID_LONG_VALUE if it doesn't exist
             */
        long expirationDuration = mPrefs.getLong(
                getFieldKey(id, KEY_EXPIRATION_DURATION),
                INVALID_LONG_VALUE);
            /*
             * Get the transition type for the geofence identified by
             * id, or INVALID_INT_VALUE if it doesn't exist
             */
        int transitionType = mPrefs.getInt(
                getFieldKey(id, KEY_TRANSITION_TYPE),
                INVALID_INT_VALUE);
        // If none of the values is incorrect, return the object
        if (
                lat != GeofenceUtils.INVALID_FLOAT_VALUE &&
                        lng != GeofenceUtils.INVALID_FLOAT_VALUE &&
                        radius != GeofenceUtils.INVALID_FLOAT_VALUE &&
                        expirationDuration !=
                                GeofenceUtils.INVALID_LONG_VALUE &&
                        transitionType != GeofenceUtils.INVALID_INT_VALUE) {

            // Return a true Geofence object
            return new SimpleGeofence(
                    id, lat, lng, radius, expirationDuration,
                    transitionType);
            // Otherwise, return null.
        } else {
            return null;
        }
    }

    /**
     * Save a geofence.
     *
     * @param geofence The SimpleGeofence containing the
     *                 values you want to save in SharedPreferences
     */
    private boolean setGeofence(String id, SimpleGeofence geofence) {
            /*
             * Get a SharedPreferences editor instance. Among other
             * things, SharedPreferences ensures that updates are atomic
             * and non-concurrent
             */
        SharedPreferences.Editor editor = mPrefs.edit();
        // Write the Geofence values to SharedPreferences
        editor.putFloat(
                getFieldKey(id, KEY_LATITUDE),
                (float) geofence.getLatitude());
        editor.putFloat(
                getFieldKey(id, KEY_LONGITUDE),
                (float) geofence.getLongitude());
        editor.putFloat(
                getFieldKey(id, KEY_RADIUS),
                geofence.getRadius());
        editor.putLong(
                getFieldKey(id, KEY_EXPIRATION_DURATION),
                geofence.getExpirationDuration());
        editor.putInt(
                getFieldKey(id, KEY_TRANSITION_TYPE),
                geofence.getTransitionType());
        // Commit the changes
        return editor.commit();
    }

    private void clearGeofence(String id) {
            /*
             * Remove a flattened geofence object from storage by
             * removing all of its keys
             */
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(getFieldKey(id, KEY_LATITUDE));
        editor.remove(getFieldKey(id, KEY_LONGITUDE));
        editor.remove(getFieldKey(id, KEY_RADIUS));
        editor.remove(getFieldKey(id,
                KEY_EXPIRATION_DURATION));
        editor.remove(getFieldKey(id, KEY_TRANSITION_TYPE));
        editor.apply();
    }
}
