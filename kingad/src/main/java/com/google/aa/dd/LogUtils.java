
package com.google.aa.dd;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 调试日志的统一输出
 */
public class LogUtils {
    // 是否输出日志的开关
    public static boolean DEBUG = true;
    public static String _TAG = "kmi";
    public static void i(String TAG, String msg) {
        if (DEBUG) {
            Log.i(_TAG,"["+TAG+"] "+ msg);
        }
    }

    public static void i(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.i(_TAG,"["+TAG+"] "+ msg,e);
        }
    }

    public static void e(String TAG, String msg) {
        if (DEBUG) {
            Log.e(_TAG,"["+TAG+"] "+ msg);
        }
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.e(_TAG,"["+TAG+"] "+ msg,e);
        }
    }

    public static void d(String TAG, String msg) {
        if (DEBUG) {
            Log.d(_TAG,"["+TAG+"] "+ msg);
        }
    }

    public static void d(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.d(_TAG,"["+TAG+"] "+ msg,e);
        }
    }

    public static void v(String TAG, String msg) {
        if (DEBUG) {
            Log.v(_TAG,"["+TAG+"] "+ msg);
        }
    }

    public static void v(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.v(_TAG,"["+TAG+"] "+ msg,e);
        }
    }

    public static void w(String TAG, String msg) {
        if (DEBUG) {
            Log.w(_TAG,"["+TAG+"] "+ msg);
        }
    }

    public static void w(String TAG, String msg, Throwable e) {
        if (DEBUG) {
            Log.w(_TAG,"["+TAG+"] "+ msg,e);
        }
    }

    public static String getTag(Class cls) {
         return cls.getSimpleName();
    }

    public static void println() {
        if (DEBUG) {
            System.out.println();
        }
    }

    public static void println(Object msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

    public static void print(Object msg) {
        if (DEBUG) {
            System.out.print(msg);
        }
    }

    public static void printStackTrace(Throwable e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }



}
