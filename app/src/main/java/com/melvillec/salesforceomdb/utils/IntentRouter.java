package com.melvillec.salesforceomdb.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;

import com.melvillec.salesforceomdb.ui.activities.MovieDetailActivity;

public class IntentRouter {

    public static final String INTENT_EXTRA_IMDB_ID = "extra_imdb_id";

    public static void launchDetailActivity(Activity activity, String imdbID) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_IMDB_ID, imdbID);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
