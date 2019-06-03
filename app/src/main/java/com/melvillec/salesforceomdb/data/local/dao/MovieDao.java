package com.melvillec.salesforceomdb.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertMovies(List<MovieEntity> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(MovieEntity movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateMovie(MovieEntity movie);

    @Query("SELECT * FROM `MovieEntity` where imdbID = :id")
    MovieEntity getMovieById(String id);

    @Query("SELECT * FROM `MovieEntity` where imdbID = :id")
    Flowable<MovieEntity> getMovieDetailById(String id);

    @Query("SELECT * FROM `MovieEntity` where isFavorite = :isFavorite")
    List<MovieEntity> getMovieFavorites(boolean isFavorite);

    @Query("SELECT * FROM `MovieEntity` where title LIKE :title and page = :page")
    List<MovieEntity> searchMoviesByTitle(String title, Long page);

}
