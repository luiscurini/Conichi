package com.luisrubenrodriguez.conichi.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by GamingMonster on 05.05.2017.
 * Basic object that will be created from the JSON response using Retrofit and Gson as converter
 */

public class Station implements Parcelable {

    private Long id;
    private String title;
    private String subtitle;
    private Double lat;
    private Double lon;
    private List<String> nextBuses;

    public Station(Long id, String title, String subtitle, Double lat, Double lon, List<String> nextBuses) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.lat = lat;
        this.lon = lon;
        this.nextBuses = nextBuses;
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    protected Station(Parcel in) {
        id = in.readLong();
        title = in.readString();
        subtitle = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        nextBuses = in.createStringArrayList();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIdTitle() {
        return id + " - " + title;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setNextBuses(List<String> nextBuses) {
        this.nextBuses = nextBuses;
    }

    public List<String> getNextBuses() {
        return nextBuses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeStringList(nextBuses);
    }
}
