package com.pdavid.android.widget.bulb.geo;

import android.os.Bundle;

import com.google.android.gms.location.Geofence;
import com.pdavid.android.widget.bulb.utils.persistance.AbstractStore;

/**
 * A single Geofence object, defined by its center and radius.
 */
public class SimpleGeofence {
    // Instance variables
    private String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;

    /**
     * @param geofenceId The Geofence's request ID
     * @param latitude   Latitude of the Geofence's center.
     * @param longitude  Longitude of the Geofence's center.
     * @param radius     Radius of the geofence circle.
     * @param expiration Geofence expiration duration
     * @param transition Type of Geofence transition.
     */
    public SimpleGeofence(
            String geofenceId,
            double latitude,
            double longitude,
            float radius,
            long expiration,
            int transition) {
        // Set the instance fields from the constructor
        this.mId = geofenceId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
        this.mExpirationDuration = expiration;
        this.mTransitionType = transition;
    }

    public SimpleGeofence(Bundle extras) {
        this(extras.getString(AbstractStore.KEY_ID), extras.getDouble(GeofenceUtils.KEY_LATITUDE), extras.getDouble(GeofenceUtils.KEY_LONGITUDE),
                extras.getFloat(GeofenceUtils.KEY_RADIUS), extras.getLong(GeofenceUtils.KEY_EXPIRATION_DURATION), extras.getInt(GeofenceUtils.KEY_TRANSITION_TYPE));
    }

    // Instance field getters
    public String getId() {
        return mId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public float getRadius() {
        return mRadius;
    }

    public long getExpirationDuration() {
        return mExpirationDuration;
    }

    public int getTransitionType() {
        return mTransitionType;
    }

    /**
     * Creates a Location Services Geofence object from a
     * SimpleGeofence.
     *
     * @return A Geofence object
     */
    public Geofence toGeofence() {
        // Build a new Geofence object
        return new Geofence.Builder()
                .setRequestId(getId())
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(
                        getLatitude(), getLongitude(), getRadius())
                .setExpirationDuration(mExpirationDuration)
                .build();
    }

    public void setId(String id) {
        this.mId = id;
    }
}
