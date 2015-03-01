package com.quester.experiment.dagger2experiment.util;

import android.util.Log;

import com.quester.experiment.dagger2experiment.BuildConfig;

import java.util.IllegalFormatException;

/**
 * Created by Josip on 16/01/2015!
 */
public class Logger {

    public static final String TAG = "Logger";

    public static void verbose(String tag, String message, Object... parameters) {
        if (BuildConfig.DEBUG) {
            try {
                Log.v(tag, String.format(message, parameters));
            } catch (NullPointerException exception) {
                Log.w(TAG, "null message sent to verbose logger from " + tag);
            } catch (IllegalFormatException exception) {
                Log.w(TAG, "illegal format sent to verbose logger from " + tag + ", original format=\"" + message + "\"");
            }
        }
    }

    public static void info(String tag, String message, Object... parameters) {
        if (BuildConfig.DEBUG) {
            try {
                Log.i(tag, String.format(message, parameters));
            } catch (NullPointerException exception) {
                Log.w(TAG, "null message sent to info logger from " + tag);
            } catch (IllegalFormatException exception) {
                Log.w(TAG, "illegal format sent to info logger from " + tag + ", original format=\"" + message + "\"");
            }
        }
    }

    public static void debug(String tag, String message, Object... parameters) {
        try {
            Log.d(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.d(TAG, "null message sent to debug logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.d(TAG, "illegal format sent to debug logger from " + tag + ", original format=\"" + message + "\"");
        }
    }

    public static void warning(String tag, String message, Object... parameters) {
        try {
            Log.w(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.w(TAG, "null message sent to warning logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.w(TAG, "illegal format sent to warning logger from " + tag + ", original format=\"" + message + "\"");
        }
    }

    public static void error(String tag, String message, Object... parameters) {
        try {
            Log.e(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.e(TAG, "null message sent to error logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.e(TAG, "illegal format sent to error logger from " + tag + ", original format=\"" + message + "\"");
        }
    }

    public static void wtf(String tag, String message, Object... parameters) {
        try {
            Log.wtf(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.wtf(TAG, "null message sent to WTF logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.wtf(TAG, "illegal format sent to WTF logger from " + tag + ", original format=\"" + message + "\"");
        }
    }
}
