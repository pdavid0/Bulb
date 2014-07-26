package com.pdavid.android.widget.bulb.activities;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pdavid.android.widget.bulb.BulbWidget;
import com.pdavid.android.widget.bulb.R;
import com.pdavid.android.widget.bulb.adapters.LightsAdapter;

import java.util.ArrayList;
import java.util.List;

import lifx.java.android.client.LFXClient;
import lifx.java.android.light.LFXLight;
import lifx.java.android.light.LFXLightCollection;
import lifx.java.android.light.LFXTaggedLightCollection;
import lifx.java.android.network_context.LFXNetworkContext;


/**
 * The configuration screen for the {@link com.pdavid.android.widget.bulb.BulbWidget BulbWidget} AppWidget.
 */
public class BulbWidgetConfigureActivity extends ListActivity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private static final String PREFS_NAME = "com.pdavid.android.widget.bulb.BulbWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private LFXNetworkContext localNetworkContext;
    private LightsAdapter adapter;
    private LFXLightCollection mAllLights;

    public BulbWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        localNetworkContext = LFXClient.getSharedInstance(this).getLocalNetworkContext();
        localNetworkContext.connect();
        mAllLights = localNetworkContext.getAllLightsCollection();
        mAllLights.getLights();
        localNetworkContext.scanNetworkForLightStates();

        localNetworkContext.addNetworkContextListener(new LFXNetworkContext.LFXNetworkContextListener() {
            @Override
            public void networkContextDidConnect(LFXNetworkContext networkContext) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void networkContextDidDisconnect(LFXNetworkContext networkContext) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void networkContextDidAddTaggedLightCollection(LFXNetworkContext networkContext, LFXTaggedLightCollection collection) {

            }

            @Override
            public void networkContextDidRemoveTaggedLightCollection(LFXNetworkContext networkContext, LFXTaggedLightCollection collection) {

            }
        });

        mAllLights.getLights();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<LFXLight> mAllLightsLights = mAllLights.getLights();
        if (mAllLightsLights.size() > 0) {
            saveList(mAllLightsLights);
        } else {
            mAllLightsLights = getList();
        }
        adapter = new LightsAdapter(BulbWidgetConfigureActivity.this, mAllLightsLights);
        setListAdapter(adapter);
    }

    private void saveList(ArrayList<LFXLight> mAllLightsLights) {
        SharedPreferences.Editor prefs = getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString("last_seen_lights", new Gson().toJson(mAllLightsLights));
        prefs.commit();
    }

    private ArrayList<LFXLight> getList() {
        return new Gson().fromJson(getSharedPreferences(PREFS_NAME, 0).getString("last_seen_light", "[]"), new TypeToken<List<LFXLight>>() {
        }.getType());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        LFXLight item = (LFXLight) l.getItemAtPosition(position);
        final Context context = BulbWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        String widgetText = item.getDeviceID();
        saveTitlePref(context, mAppWidgetId, widgetText);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        BulbWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
        super.onListItemClick(l, v, position, id);

    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}



