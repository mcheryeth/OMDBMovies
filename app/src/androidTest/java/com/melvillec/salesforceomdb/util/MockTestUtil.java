package com.melvillec.salesforceomdb.util;

import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.model.MovieApiResponse;

import java.util.ArrayList;
import java.util.List;

public class MockTestUtil {


    public static MovieApiResponse mockMovieApiResponse() {
        MovieApiResponse movieApiResponse = new MovieApiResponse();
        movieApiResponse.setMovieEntities(mockMovieList());
        return movieApiResponse;
    }

    public static List<MovieEntity> mockMovieList() {
        List<MovieEntity> movieEntities = new ArrayList<>();

        movieEntities.add(mockMovie("a1234", "Movie 1", "2001"));
        movieEntities.add(mockMovie("a4567", "Movie 2", "2003"));

        return movieEntities;
    }

    public static MovieEntity mockMovie(String imdbID, String title, String year) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setImdbID(imdbID);
        movieEntity.setTitle(title);
        movieEntity.setYear(year);
        movieEntity.setPage(1L);
        return movieEntity;
    }


    public static MovieEntity mockMovieDetail(String imdbID, String title, String year) {
        MovieEntity movieEntity = mockMovie(imdbID, title, year);
        movieEntity.setFavorite(true);
        return movieEntity;
    }
}