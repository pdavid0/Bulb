package com.pdavid.android.widget.bulb.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.geo.GeofenceUtils;
import com.pdavid.android.widget.bulb.utils.Constants;
import com.pdavid.android.widget.bulb.utils.persistance.AbstractStore;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Philippe
 */
public class GeofenceCreationDialogActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long SECONDS_PER_HOUR = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
                    SECONDS_PER_HOUR *
                    MILLISECONDS_PER_SECOND;
    public static final int ZOOM = 17;
    private static final String TAG = "Geofence Creation Activity";

    @InjectView(R.id.fragment_main_add_geofence_name)
    EditText mFragmentMainAddGeofenceName;
    @InjectView(R.id.fragment_main_add_geofence_radius)
    EditText mFragmentMainAddGeofenceRadius;

    @InjectView(R.id.fragment_main_add_geofence_action_cancel)
    Button mFragmentMainAddGeofenceActionCancel;
    @InjectView(R.id.fragment_main_add_geofence_action_ok)
    Button mFragmentMainAddGeofenceActionOk;

    private GoogleMap mMap;
    private Circle circle;
    // Global variable to hold the current location
    Location mCurrentLocation;
    private double mRadius = 50;
    private String mTitle = "Home";
    private GoogleApiClient mApiClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_dialog_create_geo);
        ButterKnife.inject(this);

        setResult(RESULT_CANCELED);

        setTitle("Create a Geofence");

        mFragmentMainAddGeofenceRadius.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (circle != null) {
                    CharSequence text = ((EditText) v).getText();
                    Double radius = Double.valueOf(text.toString());
                    mRadius = radius;
                    circle.setRadius(radius);
                }
            }
        });
        mFragmentMainAddGeofenceName.setText(mTitle);
        mFragmentMainAddGeofenceRadius.setText("" + mRadius);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    @OnClick(R.id.fragment_main_add_geofence_action_ok)
    public void onActionOK() {
        //TODO: save to DB
        Intent data = new Intent();

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mApiClient);
        }
        if (mCurrentLocation != null) {
            data.putExtra(AbstractStore.KEY_ID, mFragmentMainAddGeofenceRadius.getText().toString());
            data.putExtra(GeofenceUtils.KEY_LATITUDE, mCurrentLocation.getLatitude());
            data.putExtra(GeofenceUtils.KEY_LONGITUDE, mCurrentLocation.getLongitude());
            data.putExtra(GeofenceUtils.KEY_RADIUS, Float.valueOf(mFragmentMainAddGeofenceRadius.getText().toString()));
            data.putExtra(GeofenceUtils.KEY_EXPIRATION_DURATION, GEOFENCE_EXPIRATION_TIME);
            data.putExtra(GeofenceUtils.KEY_TRANSITION_TYPE, Geofence.GEOFENCE_TRANSITION_ENTER |
                    Geofence.GEOFENCE_TRANSITION_EXIT);
        } else {
            Toast.makeText(this, "Coudn't get location ...", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK, data);
        finish();
    }


    @OnClick(R.id.fragment_main_add_geofence_action_cancel)
    public void onActionCancel() {
        finish();
    }

    /**
     * Location & Maps API
     */
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

    @Override
    public void onConnected(Bundle bundle) {


        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mApiClient);

        if (mCurrentLocation != null) {
            updateMap();
        } else {
            //ask user to open location services
            new AlertDialog.Builder(this)
                    .setTitle("Location service disabled")
                    .setMessage("Bulb work best with location open ! Would you like to open them ?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent gpsOptionsIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                        }
                    }).create().show();
        }
//        Toast.makeText(this, getString(R.string.start_geofence_service), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If the error has a resolution, start a Google Play services activity to resolve it.
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Exception while resolving connection error.", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(TAG, "Connection to Google Play services failed with error code " + errorCode);
        }
    }
}
