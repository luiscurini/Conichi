package com.luisrubenrodriguez.conichi.Model.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.luisrubenrodriguez.conichi.Model.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GamingMonster on 05.05.2017.
 * Singleton Retrofit client
 */

public class ZaragozaAPIImplementation implements Callback<List<Station>> {

    /**
     * Interface that callback class must implement.
     */
    public interface OnDataAvailable {
        void onDataFailure();

        void onDataAvailable(List<Station> stations);
    }

    /**
     * Interface that a class must implement to receive the list of next buses coming to a station.
     */
    public interface OnBusDataAvailable {
        void onBusDataAvailable(List<String> nextBuses, int position);

        void onBusDataError(int position);
    }

    private static ZaragozaAPIImplementation mRetrofitClient;
    private ZaragozaAPI mZaragozaAPI;
    private OnDataAvailable mCallback;

    private ZaragozaAPIImplementation() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Station>>() {
                }.getType(), new StationTypeAdapter())
                .registerTypeAdapter(new TypeToken<List<String>>() {
                }.getType(), new NextBusesTypeAdapter())
                .create();

        Retrofit mRetrofitClient = new Retrofit.Builder()
                .baseUrl(ZaragozaAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mZaragozaAPI = mRetrofitClient.create(ZaragozaAPI.class);
    }

    /**
     * @return singleton instance
     */
    public static ZaragozaAPIImplementation getInstance() {
        if (null == mRetrofitClient) {
            mRetrofitClient = new ZaragozaAPIImplementation();
        }
        return mRetrofitClient;
    }

    /**
     * This will make an async request to the API, retrieves the list of stations
     *
     * @param mCallback Class that implements OnDataAvailable interface
     */
    public void getStations(OnDataAvailable mCallback) {
        this.mCallback = mCallback;
        Call<List<Station>> callGetStations = mZaragozaAPI.getStations();
        callGetStations.enqueue(this);
    }

    /**
     * This will make an async request to the API, retrieves the list of buses that will come to a particular station
     *
     * @param id       station id
     * @param position position in the recycler view list.
     * @param callback Class that implements OnBusDataAvailable.
     */
    public void getNextBuses(Long id, final Integer position, final OnBusDataAvailable callback) {
        Call<List<String>> callGetNextBuses = mZaragozaAPI.getNextBuses(id.toString());
        callGetNextBuses.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> nextBuses = response.body();
                    callback.onBusDataAvailable(nextBuses, position);
                } else {
                    callback.onBusDataError(position);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onBusDataError(position);
            }
        });
    }

    @Override
    public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
        if (response.isSuccessful()) {
            List<Station> stations = response.body();
            mCallback.onDataAvailable(stations);
        } else {
            mCallback.onDataFailure();
        }
    }

    @Override
    public void onFailure(Call<List<Station>> call, Throwable t) {
        //t.printStackTrace();
        mCallback.onDataFailure();
    }


}
