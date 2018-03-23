package leduyhung.me.animemoment.util;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class ToastUtil {

    private static ToastUtil toastUtil;
    private Toast toast;

    public static ToastUtil newInstance() {

        if (toastUtil == null)
            toastUtil = new ToastUtil();
        return toastUtil;
    }

    public ToastUtil() {

    }

    public void showToast(Context mContext, String message, int time) {

        if (toast == null) {

            toast = new Toast(mContext);
        }
        toast.cancel();
        toast = Toast.makeText(mContext, message, time);
        toast.show();
    }
}
