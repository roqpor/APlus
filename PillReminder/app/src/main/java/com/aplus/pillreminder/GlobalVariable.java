package com.aplus.pillreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalVariable {

    public static final String PREF_IS_ENABLED= "isEnabled";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setIsEnabled(Context ctx, boolean isEnabled)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_IS_ENABLED, isEnabled);
        editor.commit();
    }

    public static boolean getIsEnabled(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_ENABLED, true);
    }

    public static void clearGlobalVariable(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
