
package com.melvillec.salesforceomdb.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating implements Parcelable
{

    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("Value")
    @Expose
    private String value;
    public final static Creator<Rating> CREATOR = new Creator<Rating>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        public Rating[] newArray(int size) {
            return (new Rating[size]);
        }

    }
    ;

    protected Rating(Parcel in) {
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.value = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Rating() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(source);
        dest.writeValue(value);
    }

    public int describeContents() {
        return  0;
    }

}
