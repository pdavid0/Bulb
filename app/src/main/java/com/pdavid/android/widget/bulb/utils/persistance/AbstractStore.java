package com.pdavid.android.widget.bulb.utils.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lifx.java.android.light.LFXLight;

/**
 * @author pdavid
 */
public abstract class AbstractStore<T> {
    // The prefix for flattened geofence keys
    public static final String KEY_PREFIX =
            "com.pdavid.android.persistance.KEY";
    public static final String KEY_ID = "com.pdavid.android.persistance.KEY_ID";
    // The SharedPreferences object in which data are stored
    protected final SharedPreferences mPrefs;
    private String nextID;

    protected AbstractStore(Context context) {
        mPrefs =
                context.getSharedPreferences(
                        getStoreName(),
                        Context.MODE_PRIVATE);
        //TODO: redo ID creation
        nextID = String.valueOf(list().size()+System.currentTimeMillis());

    }

    protected void saveID(String id) {
        mPrefs.edit().putString(KEY_ID, id).apply();
    }

    protected abstract String getStoreName();

    public abstract T get(String id);

    public List<T> list() {
        final List<T> mList = new ArrayList<T>();
        Set<? extends Map.Entry<String, ?>> iterator = mPrefs.getAll().entrySet();
        for (Map.Entry<String, ?> e : iterator) {
            final String key = e.getKey();
            if (key.contains(KEY_ID)) {
                mList.add(get((String) e.getValue()));
            }
        }

        return mList;
    }

    public abstract boolean store(T object);

    public boolean storeAll(Collection<T> object) {
        for (T t : object) {
            if (!store(t)) return false;
        }
        return true;
    }


    public abstract boolean update(T object);

    public abstract boolean delete(String id);

    /**
     * Given a Geofence object's ID and the name of a field
     * (for example, KEY_LATITUDE), return the key name of the
     * object's values in SharedPreferences.
     *
     * @param id        The ID of a Geofence object
     * @param fieldName The field represented by the key
     * @return The full key name of a value in SharedPreferences
     */
    String getFieldKey(String id,
                       String fieldName) {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }

    /**
     * Creates an ID based on nextID
     * @return The id created
     */
    protected String createId() {
        return String.valueOf(nextID + 1);
    }
}
