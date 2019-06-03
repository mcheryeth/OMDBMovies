package com.melvillec.salesforceomdb.di.modules;

import com.melvillec.salesforceomdb.ui.activities.MovieDetailActivity;
import com.melvillec.salesforceomdb.ui.activities.MovieTabbedActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MovieTabbedActivity contributeMovieTabbedActivity();

    @ContributesAndroidInjector()
    abstract MovieDetailActivity contributeMovieDetailActivity();
}