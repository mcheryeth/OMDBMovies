package com.melvillec.salesforceomdb.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.Resource;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.data.remote.model.MovieApiResponse;
import com.melvillec.salesforceomdb.util.MockTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieRepositoryTest {

    @Mock
    MovieDao movieDao;

    @Mock
    MovieApiService movieApiService;

    private MovieRepository repository;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        repository = new MovieRepository(movieDao, movieApiService);
    }


    @Test
    public void searchMoviesList() {

        List<MovieEntity> loadFromDB = MockTestUtil.mockMovieList();
        when(movieDao.searchMoviesByTitle("%tata%", 1L)).thenReturn(loadFromDB);

        MovieApiResponse mockResponse = MockTestUtil.mockMovieApiResponse();
        when(movieApiService.searchMoviesByTitle("tata", 1L))
                .thenReturn(Observable.just(mockResponse));

        Observable<Resource<List<MovieEntity>>>
                data = repository.searchMoviesByTitle("tata", 1L);
        verify(movieDao).searchMoviesByTitle("%tata%", 1L);
        verify(movieApiService).searchMoviesByTitle("tata", 1L);

        TestObserver testObserver = new TestObserver();
        data.subscribe(testObserver);
        testObserver.assertNoErrors();
    }

    @Test
    public void getFavoritesList() {

        List<MovieEntity> loadFromDB = new ArrayList<>();
        when(movieDao.getMovieFavorites(true)).thenReturn(loadFromDB);

        Observable<Resource<List<MovieEntity>>>
                data = repository.fetchFavorites();
        verify(movieDao).getMovieFavorites(true);

        TestObserver testObserver = new TestObserver();
        data.subscribe(testObserver);
        testObserver.assertNoErrors();
    }

    @Test
    public void getMovieDetails() {

        Flowable<MovieEntity> loadFromDB = Flowable.just(MockTestUtil.mockMovieList().get(0));
        when(movieDao.getMovieDetailById("tt0268380")).thenReturn(loadFromDB);

        MovieEntity mockMovie = MockTestUtil.mockMovieList().get(0);
        when(movieApiService.getMovieDetails("tt0268380", "full"))
                .thenReturn(Observable.just(mockMovie));

        Observable<Resource<MovieEntity>>
                data = repository.getMovieDetails("tt0268380", "full");
        verify(movieDao).getMovieById("tt0268380");
        verify(movieApiService).getMovieDetails("tt0268380", "full");

        TestObserver testObserver = new TestObserver();
        data.subscribe(testObserver);
        testObserver.assertNoErrors();
    }
}
