
package com.melvillec.salesforceomdb.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.melvillec.salesforceomdb.data.local.entity.MovieEntity;

import java.util.List;

public class MovieApiResponse implements Parcelable
{

    @SerializedName("Search")
    @Expose
    private List<MovieEntity> movieEntities;

    @SerializedName("totalResults")
    @Expose
    private String totalResults;

    @SerializedName("Response")
    @Expose
    private String response;

    public final static Creator<MovieApiResponse> CREATOR = new Creator<MovieApiResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MovieApiResponse createFromParcel(Parcel in) {
            return new MovieApiResponse(in);
        }

        public MovieApiResponse[] newArray(int size) {
            return (new MovieApiResponse[size]);
        }

    }
    ;

    protected MovieApiResponse(Parcel in) {
        in.readList(this.movieEntities, (MovieEntity.class.getClassLoader()));
        this.totalResults = ((String) in.readValue((String.class.getClassLoader())));
        this.response = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MovieApiResponse() {
    }

    public List<MovieEntity> getMovieEntities() {
        return movieEntities;
    }

    public void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(movieEntities);
        dest.writeValue(totalResults);
        dest.writeValue(response);
    }

    public int describeContents() {
        return  0;
    }

}
