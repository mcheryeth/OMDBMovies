package com.melvillec.salesforceomdb.data.remote.api;


import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.model.MovieApiResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("/")
    Observable<MovieApiResponse> searchMoviesByTitle(@Query("s") String searchQuery,
                                                   @Query("page") long page);

    @GET("/")
    Call<MovieApiResponse> searchMovies(@Query("s") String searchQuery,
                                               @Query("page") long page);


    @GET("/")
    Observable<MovieEntity> getMovieDetails(@Query("i") String imdbID,
                                            @Query("plot") String plot);
}
