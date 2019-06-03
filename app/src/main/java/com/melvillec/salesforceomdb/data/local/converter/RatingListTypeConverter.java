package com.melvillec.salesforceomdb.data.local.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melvillec.salesforceomdb.data.local.entity.Rating;

import java.lang.reflect.Type;
import java.util.List;

public class RatingListTypeConverter {

    @TypeConverter
    public List<Rating> fromString(String value) {
        Type listType = new TypeToken<List<Rating>>() {}.getType();
        List<Rating> ratings = new Gson().fromJson(value, listType);
        return ratings;
    }

    @TypeConverter
    public String fromList(List<Rating> ratings) {
        return new Gson().toJson(ratings);
    }
}
