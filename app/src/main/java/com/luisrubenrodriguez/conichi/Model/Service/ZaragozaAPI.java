package com.luisrubenrodriguez.conichi.Model.Service;

import com.luisrubenrodriguez.conichi.Model.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by GamingMonster on 05.05.2017.
 * Retrofit Interface. It will call the Zaragoza API.
 */

public interface ZaragozaAPI {
    String BASE_URL = "http://api.dndzgz.com/";

    @GET("/services/bus")
    Call<List<Station>> getStations();

    @GET("/services/bus/{station_id}")
    Call<List<String>> getNextBuses(@Path("station_id") String station_id);
}
