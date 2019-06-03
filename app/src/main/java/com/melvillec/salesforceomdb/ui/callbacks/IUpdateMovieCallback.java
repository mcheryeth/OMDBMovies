package com.melvillec.salesforceomdb.ui.callbacks;

import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;

public interface IUpdateMovieCallback {
    void onMovieUpdated(MovieEntity movieEntity);
}
