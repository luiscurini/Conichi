package com.luisrubenrodriguez.conichi;

import com.luisrubenrodriguez.conichi.Model.Station;

import java.util.List;

/**
 * Created by GamingMonster on 05.05.2017.
 * MainView Interface
 */

interface MainView {

    void showProgress();

    void hideProgress();

    void showApartmentList(List<Station> stations);

    void showConnectionError();
}
