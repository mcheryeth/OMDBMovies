package com.melvillec.salesforceomdb.ui.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.Resource;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.data.repository.MovieRepository;
import com.melvillec.salesforceomdb.ui.callbacks.IUpdateMovieCallback;

import java.util.List;

import javax.inject.Inject;

public class MovieSearchListViewModel extends BaseViewModel implements IUpdateMovieCallback {

    @Inject
    public MovieSearchListViewModel(MovieDao movieDao, MovieApiService movieApiService) {
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<List<MovieEntity>>> moviesLiveData = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public void loadMovies(String title, Long currentPage) {
        movieRepository.searchMoviesByTitle(title, currentPage,
                errorResource -> { getMoviesLiveData().postValue(errorResource); })
                    .doOnSubscribe(this::addToDisposable)
                    .subscribe(successResource -> getMoviesLiveData().postValue(successResource));
    }

    @Override
    public void onMovieUpdated(MovieEntity movieEntity) {
        movieRepository.updateMovie(movieEntity);
    }

    public boolean isLastPage() {
        return (moviesLiveData.getValue() != null &&
                moviesLiveData.getValue().data != null && !moviesLiveData.getValue().data.isEmpty())
                && moviesLiveData.getValue().data.get(0).isLastPage();
    }

    public MutableLiveData<Resource<List<MovieEntity>>> getMoviesLiveData() {
        return moviesLiveData;
    }
}