package com.melvillec.salesforceomdb.di.modules;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.melvillec.salesforceomdb.data.local.AppDatabase;
import com.melvillec.salesforceomdb.data.local.dao.MovieDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "OMDB.db")
                .allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.movieDao();
    }
}
