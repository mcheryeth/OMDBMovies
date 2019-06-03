package com.melvillec.salesforceomdb.utils;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static void hideKeyboard(Activity activity) {
        try {
            final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                if (activity.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            Log.e(Utils.class.getSimpleName(), "Caught exception forcing keyboard closed - " + e.toString());
        }
    }
}
