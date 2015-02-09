package com.quester.experiment.dagger2experiment.engine.processor.javascript;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;

import static org.mozilla.javascript.NativeJSON.parse;

/**
 * Created by Josip on 24/01/2015!
 */
public class ScriptableConverter {

    private static final Callable IDENTITY_REVIVER = new Callable() {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            return args[1];
        }
    };

    public static Scriptable getScriptableFromString(final String scriptableString, final Scriptable sharedScope) {
        try {
            return (Scriptable) parse(Context.enter(), sharedScope, scriptableString, IDENTITY_REVIVER);
        } finally {
            Context.exit();
        }
    }

    public static String getStringFromScriptable(final Scriptable scriptableObject, final Scriptable sharedScope) {
        try {
            return NativeJSON.stringify(Context.enter(), sharedScope, scriptableObject, null, null).toString();
        } finally {
            Context.exit();
        }
    }

}
