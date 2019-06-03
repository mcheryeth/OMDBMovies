package com.melvillec.salesforceomdb.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.melvillec.salesforceomdb.factory.ViewModelFactory;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieDetailViewModel;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieFavoriteListViewModel;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieSearchListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieSearchListViewModel.class)
    protected abstract ViewModel searchMovieListViewModel(MovieSearchListViewModel movieSearchListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieFavoriteListViewModel.class)
    protected abstract ViewModel favoritesMovieListViewModel(MovieFavoriteListViewModel movieFavoriteListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    protected abstract ViewModel movieDetailViewModel(MovieDetailViewModel movieDetailViewModel);

}