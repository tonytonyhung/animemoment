package leduyhung.me.animemoment.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.leduyhung.loglibrary.Logg;

import java.util.ArrayList;
import java.util.List;

import leduyhung.me.animemoment.R;

public class ClientUtil {

    public static final String APP_SETTING_VIBRATE = "APP_VIBRATE";
    public static final String APP_SETTING_RING = "APP_RING";

    /**
     * get screen size device
     * getScreenSize.heightPixel -> get height
     * getScreenSize.widthPixel -> get width
     *
     * @param mContext
     * @return
     */
    public static DisplayMetrics getScreenSize(Activity mContext) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static String getDeviceId(Context mContext) {

        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getModelDevice() {

        return Build.MODEL;
    }

    public static String getManufactureDevice() {

        return Build.MANUFACTURER;
    }

    public static String getAndroidApi() {

        return Build.VERSION.RELEASE;
    }

    /**
     * can return null with other version android
     *
     * @return
     */
    public static String getNameDevice() {

        String nameDevice = "null";
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            if (myDevice != null)
                nameDevice = myDevice.getName();
            else {

                nameDevice = getManufactureDevice() + " " + getAndroidApi() + " " + getModelDevice();
            }
        } catch (Exception e) {

        } finally {

            return nameDevice;
        }
    }

    public static int getVersionCode(Context mContext) {

        int versioncode = 0;
        try {
            versioncode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return versioncode;
        }
    }

    public static boolean isConnectInternet(Context mContext) {
        if (mContext == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void openWifi(Context mContext, boolean isOpen) {

        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(isOpen);
    }

    public static void openScreenWifi(Context mContext) {

        mContext.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    public static boolean isSoftKeyboardShow(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm.isAcceptingText();
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

            Logg.error(ClientUtil.class, "hideSoftKeyboard: " + e.toString());
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {

            Logg.error(ClientUtil.class, "showSoftKeyboard: " + e.toString());
        }
    }

    public static boolean youtubeAppIsInstall(Context mContext) {

        Intent mIntent = mContext.getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        if (mIntent != null) {

            return true;
        } else {

            return false;
        }
    }

    public static String getPakageName(Context mContext) {

        return mContext.getApplicationContext().getPackageName();
    }

    public static void vibarateDevice(Context mContext) {

        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(mContext.getResources().getInteger(R.integer.time_vibrate));
    }
}
