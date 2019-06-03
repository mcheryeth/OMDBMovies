package com.melvillec.salesforceomdb;

import android.app.Activity;
import android.app.Application;

import com.evernote.android.state.StateSaver;
import com.melvillec.salesforceomdb.di.components.DaggerAppComponent;
import com.melvillec.salesforceomdb.di.modules.ApiModule;
import com.melvillec.salesforceomdb.di.modules.DbModule;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MoviesApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true);

        DaggerAppComponent.builder()
                .application(this)
                .apiModule(new ApiModule())
                .dbModule(new DbModule())
                .build()
                .inject(this);

    }
}
