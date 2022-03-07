package com.adslinfosoft.softberry.app;

/**
 * Created by shreshtha on 30/05/16.
 */

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.adslinfosoft.softberry.db.DataBaseHandler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Softberry extends Application {

    private static SharedPreferences mPreferences;
    private static DataBaseHandler sDBHadler;
    public static final String TAG = Softberry.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static Softberry mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sDBHadler = new DataBaseHandler(getApplicationContext());
        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
    }

    public static synchronized Softberry getInstance() {
        return mInstance;
    }

    public static DataBaseHandler getDBHandler() {
        return sDBHadler;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static SharedPreferences getPreference() {
        return mPreferences;
    }
}