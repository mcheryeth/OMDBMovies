package com.melvillec.salesforceomdb.viewmodel;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.Resource;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.data.remote.model.MovieApiResponse;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieSearchListViewModel;
import com.melvillec.salesforceomdb.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MovieSearchListViewModelTest {

    private MovieSearchListViewModel movieSearchListViewModel;

    @Mock
    MovieDao movieDao;

    @Mock
    MovieApiService movieApiService;

    @Mock
    Observer<Resource<List<MovieEntity>>> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        Application app = (Application) ApplicationProvider.getApplicationContext().getApplicationContext();
        movieSearchListViewModel = new MovieSearchListViewModel(movieDao, movieApiService);
    }

    @Test
    public void fetchMovies() {
        List<MovieEntity> loadFromDB = MockTestUtil.mockMovieList();
        when(movieDao.searchMoviesByTitle("%tata%", 1L)).thenReturn(loadFromDB);

        MovieApiResponse mockResponse = MockTestUtil.mockMovieApiResponse();
        when(movieApiService.searchMoviesByTitle("tata", 1L))
                .thenReturn(Observable.just(mockResponse));

        movieSearchListViewModel.getMoviesLiveData().observeForever(observer);
        movieSearchListViewModel.loadMovies("tata", 1L);

        assert(movieSearchListViewModel.getMoviesLiveData().getValue().isLoading());
        assert(movieSearchListViewModel.getMoviesLiveData().getValue().data == MockTestUtil.mockMovieList());
    }
}
