package com.melvillec.salesforceomdb.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.melvillec.salesforceomdb.data.local.converter.MovieListTypeConverter;
import com.melvillec.salesforceomdb.data.local.converter.RatingListTypeConverter;
import com.melvillec.salesforceomdb.data.local.converter.StringListConverter;
import com.melvillec.salesforceomdb.data.local.dao.MovieDao;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;

@Database(entities = {MovieEntity.class}, version = 1,  exportSchema = false)
@TypeConverters({MovieListTypeConverter.class, RatingListTypeConverter.class, StringListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, "OMDB.db")
                .allowMainThreadQueries().build();
    }
}
