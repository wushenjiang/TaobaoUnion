package com.liuge.taobaounion.utils;

import android.util.Log;

/**
 * FileName: LogUtils
 * Author: LiuGe
 * Date: 2020/8/23 23:50
 * Description: 日志输出工具
 */
public class LogUtils {

    private static int currentLev = 4;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void d(Object object, String log) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }

    public static void i(Object object, String log) {
        if (currentLev >= INFO_LEV) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }

    public static void w(Object object, String log) {
        if (currentLev >= WARNING_LEV) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }

    public static void e(Object object, String log) {
        if (currentLev >= ERROR_LEV) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }

}
