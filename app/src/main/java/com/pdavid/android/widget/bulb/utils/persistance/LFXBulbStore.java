package com.pdavid.android.widget.bulb.utils.persistance;

import android.content.Context;

import lifx.java.android.light.LFXLight;

/**
 * @author pdavid
 */
public class LFXBulbStore extends AbstractStore<LFXLight>{
    protected LFXBulbStore(Context context) {
        super(context);
    }

    @Override
    protected String getStoreName() {
        return null;
    }

    @Override
    public LFXLight get(String id) {
        return null;
    }

    @Override
    public boolean store(LFXLight object) {
        return false;
    }

    @Override
    public boolean update(LFXLight object) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
