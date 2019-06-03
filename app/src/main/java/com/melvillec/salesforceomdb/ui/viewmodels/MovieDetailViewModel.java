package com.melvillec.salesforceomdb.ui.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.Resource;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.data.repository.MovieRepository;
import com.melvillec.salesforceomdb.ui.callbacks.IUpdateMovieCallback;

import javax.inject.Inject;

public class MovieDetailViewModel extends BaseViewModel implements IUpdateMovieCallback {

    @Inject
    public MovieDetailViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<MovieEntity>> movieLiveData = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public void getMovieDetails(String imdbId) {
        movieRepository.getMovieDetails(imdbId, "full")
                .doOnSubscribe(this::addToDisposable)
                .subscribe(resource -> getMovieLiveData().postValue(resource));
    }

    @Override
    public void onMovieUpdated(MovieEntity movieEntity) {
        movieRepository.updateMovie(movieEntity);
    }

    public MutableLiveData<Resource<MovieEntity>> getMovieLiveData() {
        return movieLiveData;
    }
}