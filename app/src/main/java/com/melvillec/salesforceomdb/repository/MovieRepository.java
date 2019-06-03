package com.melvillec.salesforceomdb.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.NetworkBoundResource;
import com.melvillec.salesforceomdb.data.remote.Resource;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.data.remote.model.MovieApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Singleton
public class MovieRepository {

    private MovieDao movieDao;
    private MovieApiService movieApiService;

    public MovieRepository(MovieDao movieDao,
                           MovieApiService movieApiService) {
        this.movieDao = movieDao;
        this.movieApiService = movieApiService;
    }

    public Observable<Resource<List<MovieEntity>>> searchMoviesByTitle(String title, Long page) {
        return new NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MovieApiResponse item) {
                if (item.getMovieEntities() == null || item.getMovieEntities().isEmpty()) {
                    return;
                }

                List<MovieEntity> movieEntities = new ArrayList<>();
                for (MovieEntity movieEntity : item.getMovieEntities()) {

                    MovieEntity cachedMovieEntity = movieDao.getMovieById(movieEntity.getImdbID());
                    if (cachedMovieEntity != null) {
                        movieEntity.setFavorite(cachedMovieEntity.isFavorite());
                    }

                    movieEntity.setPage(page);
                    movieEntity.setTotalPages(Long.valueOf(item.getTotalResults()));
                    movieEntities.add(movieEntity);
                }
                movieDao.insertMovies(movieEntities);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @Override
            protected void onFetchFailed(Throwable t) {
                Log.e(MovieRepository.class.getSimpleName(), t.getMessage());
            }

            @NonNull
            @Override
            protected Flowable<List<MovieEntity>> loadFromDb() {
                List<MovieEntity> movieEntities = movieDao.searchMoviesByTitle("%"+title+"%", page);
                if (movieEntities == null || movieEntities.isEmpty()) {
                    return Flowable.empty();
                }
                return Flowable.just(movieEntities);
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieApiResponse>> createCall() {
                return movieApiService.searchMoviesByTitle(title, page)
                        .flatMap(movieApiResponse -> Observable.just(movieApiResponse == null
                                ? Resource.error("", new MovieApiResponse())
                                : Resource.success(movieApiResponse)));
            }
        }.getAsObservable();
    }

    public Observable<Resource<List<MovieEntity>>> fetchFavorites() {
        List<MovieEntity> movieEntities = movieDao.getMovieFavorites(true);
        return Flowable.just(movieEntities).toObservable().map(Resource::success);
    }

    public void updateMovie(MovieEntity movieEntity) {
        Executors.newSingleThreadExecutor().execute(() ->
                movieDao.updateMovie(movieEntity));
    }

    public Observable<Resource<MovieEntity>> getMovieDetails(String imdbID, String plot) {
        return new NetworkBoundResource<MovieEntity, MovieEntity>() {
            @Override
            protected void saveCallResult(@NonNull MovieEntity item) {
                MovieEntity movieEntity = movieDao.getMovieById(item.getImdbID());
                if (movieEntity == null) {
                    movieDao.insertMovie(item);
                } else {
                    item.setFavorite(movieEntity.isFavorite());
                    item.setPage(movieEntity.getPage());
                    item.setTotalPages(movieEntity.getTotalPages());
                    movieDao.updateMovie(item);
                }
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<MovieEntity> loadFromDb() {
                MovieEntity movieEntity = movieDao.getMovieById(imdbID);
                if (movieEntity == null) {
                    return Flowable.empty();
                }
                return Flowable.just(movieEntity);
            }

            @NonNull
            @Override
            protected Observable<Resource<MovieEntity>> createCall() {
                String id = String.valueOf(imdbID);

                return movieApiService.getMovieDetails(id, plot)
                        .flatMap(movieEntity -> Observable.just(movieEntity == null
                                ? Resource.error("", new MovieEntity())
                                : Resource.success(movieEntity)));
            }
        }.getAsObservable();
    }


}
