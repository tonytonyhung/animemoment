package leduyhung.me.animemoment.util;

import android.os.Bundle;

public class TimeUtil {

    private static TimeUtil timeUtil;

    public static TimeUtil newInstance() {

        if (timeUtil == null)
            timeUtil = new TimeUtil();

        return timeUtil;
    }

    public String convertSecondToTimeString(int second) {

        int hour = second / 3600;
        int minute = second / 60;
        int se = second % 60;

        return hour + " : " + minute + " : " + se;
    }
}
