package com.melvillec.salesforceomdb.di.modules;

import com.melvillec.salesforceomdb.ui.fragments.MovieFavoriteListFragment;
import com.melvillec.salesforceomdb.ui.fragments.MovieSearchListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MovieSearchListFragment contributeMovieSearchListFragment();

    @ContributesAndroidInjector
    abstract MovieFavoriteListFragment contributeMovieFavoriteListFragment();
}
