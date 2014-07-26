package com.pdavid.android.widget.bulb.utils;

/**
 *
 * @author pdavid
 */
public class Persistence {
    static Persistence instance;

    private Persistence(){

    }

    public static Persistence getInstance() {
        if (instance ==null){
            instance = new Persistence();
        }
        return instance;
    }
}
