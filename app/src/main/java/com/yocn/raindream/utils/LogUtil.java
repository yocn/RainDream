package com.yocn.raindream.utils;

import android.util.Log;

/**
 * @Author yocn
 * @Date 2019-10-17 20:42
 * @ClassName LogUtil
 */
public class LogUtil {
    private static final String TAG = "YocnYocn";

    public static void d(String msg, Object... ss) {
        StringBuilder sb = new StringBuilder(msg);
        for (Object o : ss) {
            sb.append(o.toString());
        }
        Log.d(TAG, sb.toString());
    }

    public static void w(String msg, Object... ss) {
        StringBuilder sb = new StringBuilder(msg);
        for (Object o : ss) {
            sb.append(o.toString());
        }
        Log.d(TAG, sb.toString());

    }

    public static void i(String msg, Object... ss) {

        StringBuilder sb = new StringBuilder(msg);
        for (Object o : ss) {
            sb.append(o.toString());
        }
        Log.d(TAG, sb.toString());
    }

    public static void v(String msg, Object... ss) {

        StringBuilder sb = new StringBuilder(msg);
        for (Object o : ss) {
            sb.append(o.toString());
        }
        Log.d(TAG, sb.toString());
    }

    public static void e(String msg, Object... ss) {

        StringBuilder sb = new StringBuilder(msg);
        for (Object o : ss) {
            sb.append(o.toString());
        }
        Log.d(TAG, sb.toString());
    }


}
