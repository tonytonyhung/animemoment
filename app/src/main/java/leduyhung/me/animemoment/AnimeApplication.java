package leduyhung.me.animemoment;

import android.app.Application;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.google.android.gms.ads.MobileAds;
import com.leduyhung.loglibrary.Logg;

import leduyhung.me.animemoment.util.CacheUtil;
import leduyhung.me.animemoment.util.ClientUtil;

public class AnimeApplication extends Application {

    public static final String IS_APP_OPEN_FIRST = "IS_APP_OPEN_FIRST";

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH)
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Logg.init(getString(R.string.app_name), false);
        FacebookSdk.sdkInitialize(getApplicationContext());
        MobileAds.initialize(this, getResources().getString(R.string.ads_id));
        CacheUtil.newInstance().addContext(getApplicationContext());
    }
}