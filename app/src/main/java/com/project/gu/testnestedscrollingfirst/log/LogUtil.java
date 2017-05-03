package com.project.gu.testnestedscrollingfirst.log;

import android.util.Log;

/**
 * Created by gu on 2017/4/18.
 */

public class LogUtil {
    private static final boolean DEBUG = true;
    private static String tag = "tag";

    public static void log(String str) {
        if (DEBUG) {
            Log.w(tag, str);
        }
    }
}
