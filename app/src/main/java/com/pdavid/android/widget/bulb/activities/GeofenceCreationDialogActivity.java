package com.pdavid.android.widget.bulb.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.geo.GeofenceUtils;
import com.pdavid.android.widget.bulb.utils.persistance.AbstractStore;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Philippe
 */
public class GeofenceCreationDialogActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    /*
           * Use to set an expiration time for a geofence. After this amount
           * of time Location Services will stop tracking the geofence.
           * TODO: move to elsewhere and give the user some control
           */
    private static final long SECONDS_PER_HOUR = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
                    SECONDS_PER_HOUR *
                    MILLISECONDS_PER_SECOND;
    public static final int ZOOM = 17;

    @InjectView(R.id.fragment_main_add_geofence_name)
    FloatLabeledEditText mFragmentMainAddGeofenceName;
    @InjectView(R.id.fragment_main_add_geofence_radius)
    FloatLabeledEditText mFragmentMainAddGeofenceRadius;

    @InjectView(R.id.fragment_main_add_geofence_action_cancel)
    Button mFragmentMainAddGeofenceActionCancel;
    @InjectView(R.id.fragment_main_add_geofence_action_ok)
    Button mFragmentMainAddGeofenceActionOk;

    private GoogleMap mMap;
    private Circle circle;
    private LocationClient mLocationClient;
    // Global variable to hold the current location
    Location mCurrentLocation;
    private double mRadius = 50;
    private String mTitle = "Home";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_dialog_create_geo);
        ButterKnife.inject(this);
        setResult(RESULT_CANCELED);
        mLocationClient = new LocationClient(this, this, this);

        setTitle("Create a Geofence");

        mFragmentMainAddGeofenceRadius.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                CharSequence text = v.getText();
                Double radius = Double.valueOf(text.toString());
                mRadius = radius;
                circle.setRadius(radius);
                return true;
            }
        });
        mFragmentMainAddGeofenceName.setText(mTitle);
        mFragmentMainAddGeofenceRadius.setText("" + mRadius);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);

        Location location = mMap.getMyLocation();

        if (location != null) {
            updateMap();
        }
    }

    /*
 * Called when the Activity becomes visible.
 */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.fragment_main_add_geofence_action_ok)
    public void onActionOK() {
        //TODO: save to DB
        Intent data = new Intent();

        data.putExtra(AbstractStore.KEY_ID, mFragmentMainAddGeofenceRadius.getText().toString());
        data.putExtra(GeofenceUtils.KEY_LATITUDE, mCurrentLocation.getLatitude());
        data.putExtra(GeofenceUtils.KEY_LONGITUDE, mCurrentLocation.getLongitude());
        data.putExtra(GeofenceUtils.KEY_RADIUS, Float.valueOf(mFragmentMainAddGeofenceRadius.getText().toString()));
        data.putExtra(GeofenceUtils.KEY_EXPIRATION_DURATION, GEOFENCE_EXPIRATION_TIME);
        data.putExtra(GeofenceUtils.KEY_TRANSITION_TYPE, Geofence.GEOFENCE_TRANSITION_ENTER |
                Geofence.GEOFENCE_TRANSITION_EXIT);

        setResult(RESULT_OK, data);
        finish();
    }


    @OnClick(R.id.fragment_main_add_geofence_action_cancel)
    public void onActionCancel() {
        finish();
    }

    /**
     * Location API
     */

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = mLocationClient.getLastLocation();
        Log.w("Location", "Location connected");
        Log.w("Location", mCurrentLocation.toString());
        updateMap();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Error ? I think you can fix it :" + connectionResult.hasResolution(), Toast.LENGTH_SHORT).show();
    }

    private void updateMap() {
        final LatLng latLng = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        if (circle == null) {

            // Instantiates a new CircleOptions object and defines the center and radius

            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(mRadius); // In meters
            // Get back the mutable Circle
            this.circle = mMap.addCircle(circleOptions);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
    }
}
