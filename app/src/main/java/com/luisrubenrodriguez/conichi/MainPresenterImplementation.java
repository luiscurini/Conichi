package com.luisrubenrodriguez.conichi;

import com.luisrubenrodriguez.conichi.Model.Service.ZaragozaAPIImplementation;
import com.luisrubenrodriguez.conichi.Model.Station;

import java.util.List;

/**
 * Created by GamingMonster on 05.05.2017.
 * Main Presenter Implementation
 */

class MainPresenterImplementation implements MainPresenter, ZaragozaAPIImplementation.OnDataAvailable {


    //private static final String TAG = "MainPresenterImplementa";
    private final MainView mMainView;
    private final ZaragozaAPIImplementation mZaragozaAPI;

    MainPresenterImplementation(MainView MView, ZaragozaAPIImplementation zaragozaAPI) {
        mMainView = MView;
        mZaragozaAPI = zaragozaAPI;
    }

    /**
     * Calls the api to retrieve the list of stations
     */
    @Override
    public void loadStationData() {
        mMainView.showProgress();
        mZaragozaAPI.getStations(this);
    }

    /**
     * Something went wrong with the api call, no internet, service unavailable.
     * It shows the error message.
     */
    @Override
    public void onDataFailure() {
        mMainView.hideProgress();
        mMainView.showConnectionError();
    }

    /**
     * In case that the api call is successful, it will pass the station list to the view.
     *
     * @param stations Json response converted to object
     */
    @Override
    public void onDataAvailable(List<Station> stations) {
        mMainView.hideProgress();
        if (stations != null && stations.size() > 0) {
            mMainView.showApartmentList(stations);
        }
    }
}
