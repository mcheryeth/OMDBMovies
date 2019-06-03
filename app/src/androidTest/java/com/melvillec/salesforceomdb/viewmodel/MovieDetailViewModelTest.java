package com.melvillec.salesforceomdb.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.Resource;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.ui.viewmodels.MovieDetailViewModel;
import com.melvillec.salesforceomdb.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MovieDetailViewModelTest {

    MovieDetailViewModel movieDetailViewModel;

    @Mock
    public MovieDao movieDao;

    @Mock
    public MovieApiService movieApiService;

    @Mock
    Observer<Resource<MovieEntity>> observer;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        movieDetailViewModel = new MovieDetailViewModel(movieDao, movieApiService);
    }

    @Test
    public void loadMovieDetails() {
        MovieEntity loadFromDB = (MockTestUtil.mockMovieList().get(0));
        when(movieDao.getMovieById("a1234")).thenReturn(loadFromDB);

        MovieEntity mockMovie = MockTestUtil.mockMovieList().get(0);
        when(movieApiService.getMovieDetails("a1234", "full"))
                .thenReturn(Observable.just(mockMovie));

        movieDetailViewModel.getMovieLiveData().observeForever(observer);
        movieDetailViewModel.getMovieDetails(mockMovie.getImdbID());

        assertEquals(movieDetailViewModel.getMovieLiveData().getValue().data.getImdbID(), mockMovie.getImdbID());
    }
}
