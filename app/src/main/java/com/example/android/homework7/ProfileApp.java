package com.example.android.homework7;

import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

/**
 * Created by Jake on 11/16/16.
 */

public class ProfileApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
        MultiDex.install(this);
    }
}
