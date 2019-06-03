package com.melvillec.salesforceomdb;

import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.data.remote.api.MovieApiService;
import com.melvillec.salesforceomdb.data.remote.model.MovieApiResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MovieApiServiceTest extends ApiAbstract<MovieApiService> {

    private MovieApiService service;

    @Before
    public void initService() {
        this.service = createService(MovieApiService.class);
    }

    @Test
    public void getMovieDetailsTest() throws IOException {
        enqueueResponse("test_movie_details.json");
        MovieEntity movieEntity = service.getMovieDetails("tt0268380", "short").blockingFirst();
        Assert.assertEquals("Ice Age", movieEntity.getTitle());
        Assert.assertEquals("tt0268380", movieEntity.getImdbID());
    }

    @Test
    public void searchMoviesTest() throws IOException {
        enqueueResponse("test_movie.json");
        MovieApiResponse movieApiResponse = service.searchMoviesByTitle("ice", 1).blockingFirst();
        Assert.assertEquals("1095", movieApiResponse.getTotalResults());
        Assert.assertEquals(10, movieApiResponse.getMovieEntities().size());
        Assert.assertEquals("Cool as Ice", movieApiResponse.getMovieEntities().get(0).getTitle());
    }
}
