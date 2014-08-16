package com.pdavid.android.widget.bulb.utils.persistance;

import android.content.Context;

import java.util.Properties;

/**
 *
 * @author pdavid
 */
public class Persistence {
    static Persistence instance;
    private final SimpleGeofenceStore store;
    private LFXBulbStore mLFXBulbStore;

    public SimpleGeofenceStore getStore() {
        return store;
    }

    private Persistence(Context ctx){
        this.store = new SimpleGeofenceStore(ctx);
    }

    public static Persistence getInstance(Context context) {
        if (instance ==null){
            instance = new Persistence(context);
        }
        return instance;
    }

    public LFXBulbStore getLFXBulbStore() {
        return mLFXBulbStore;
    }

    public LFXBulbStore getmLFXBulbStore() {
        return mLFXBulbStore;
    }

    public void setmLFXBulbStore(LFXBulbStore mLFXBulbStore) {
        this.mLFXBulbStore = mLFXBulbStore;
    }
}
