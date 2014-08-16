package com.pdavid.android.widget.bulb;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.test.ApplicationTestCase;

import com.google.android.gms.location.Geofence;
import com.pdavid.android.widget.bulb.geo.GeofenceUtils;
import com.pdavid.android.widget.bulb.geo.SimpleGeofence;
import com.pdavid.android.widget.bulb.utils.persistance.Persistence;
import com.pdavid.android.widget.bulb.utils.persistance.SimpleGeofenceStore;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testStores(){
        Persistence persistence = Persistence.getInstance(getContext());
        SimpleGeofenceStore store = persistence.getStore();

        assertTrue(store.store(new SimpleGeofence("1",10,10,50,10,1)));

        assertTrue(store.list().size()>0);
    }
}