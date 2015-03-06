package com.quester.experiment.dagger2experiment.util;

import android.util.Log;

import com.quester.experiment.dagger2experiment.BuildConfig;

import java.util.IllegalFormatException;

public class Logger {

    public static Logger instance(Class clazz){
        return new Logger(clazz.getName());
    }

    private final String tag;

    public Logger(String tag) {
        this.tag = tag;
    }

    public void verbose(String message, Object... parameters) {
        if (BuildConfig.DEBUG) {
            try {
                Log.v(tag, String.format(message, parameters));
            } catch (NullPointerException exception) {
                Log.w(tag, "null message sent to verbose logger from " + tag);
            } catch (IllegalFormatException exception) {
                Log.w(tag, "illegal format sent to verbose logger from " + tag + ", original format=\"" + message + "\"");
            }
        }
    }

    public void info(String message, Object... parameters) {
        if (BuildConfig.DEBUG) {
            try {
                Log.i(tag, String.format(message, parameters));
            } catch (NullPointerException exception) {
                Log.w(tag, "null message sent to info logger from " + tag);
            } catch (IllegalFormatException exception) {
                Log.w(tag, "illegal format sent to info logger from " + tag + ", original format=\"" + message + "\"");
            }
        }
    }

    public void debug(String message, Object... parameters) {
        try {
            Log.d(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.d(tag, "null message sent to debug logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.d(tag, "illegal format sent to debug logger from " + tag + ", original format=\"" + message + "\"");
        }
    }

    public void warning(String message, Object... parameters) {
        try {
            Log.w(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.w(tag, "null message sent to warning logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.w(tag, "illegal format sent to warning logger from " + tag + ", original format=\"" + message + "\"");
        }
    }

    public void error(String message, Object... parameters) {
        try {
            Log.e(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.e(tag, "null message sent to error logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.e(tag, "illegal format sent to error logger from " + tag + ", original format=\"" + message + "\"");
        }
    }

    public void wtf(String message, Object... parameters) {
        try {
            Log.wtf(tag, String.format(message, parameters));
        } catch (NullPointerException exception) {
            Log.wtf(tag, "null message sent to WTF logger from " + tag);
        } catch (IllegalFormatException exception) {
            Log.wtf(tag, "illegal format sent to WTF logger from " + tag + ", original format=\"" + message + "\"");
        }
    }
}
