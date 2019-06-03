package com.melvillec.salesforceomdb.db;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;
import com.melvillec.salesforceomdb.util.MockTestUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MovieDaoTest extends DbTest {

    @Test
    public void insertAndReadMovieTest() {
        List<MovieEntity> movieEntities = new ArrayList<>(MockTestUtil.mockMovieList());

        db.movieDao().insertMovies(movieEntities);
        List<MovieEntity> storedMovieEntities = db.movieDao().searchMoviesByTitle("%Movie%", 1L);
        Assert.assertEquals("Movie 1", storedMovieEntities.get(0).getTitle());
        Assert.assertEquals(1L, storedMovieEntities.get(0).getPage().longValue());
    }

    @Test
    public void updateAndReadMovieTest() {
        List<MovieEntity> movieEntities = new ArrayList<>(MockTestUtil.mockMovieList());

        db.movieDao().insertMovies(movieEntities);

        MovieEntity storedMovieEntity = db.movieDao().getMovieDetailById("a1234").blockingFirst();
        Assert.assertEquals(false, storedMovieEntity.isFavorite());

        storedMovieEntity.setFavorite(true);
        db.movieDao().updateMovie(storedMovieEntity);

        MovieEntity updatedMovieEntity = db.movieDao().getMovieDetailById("a1234").blockingFirst();
        Assert.assertEquals(true, updatedMovieEntity.isFavorite());
    }
}
