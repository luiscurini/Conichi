package com.luisrubenrodriguez.conichi;

import com.luisrubenrodriguez.conichi.Model.Service.ZaragozaAPIImplementation;
import com.luisrubenrodriguez.conichi.Model.Station;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by GamingMonster on 09.05.2017.
 */
public class MainPresenterImplementationTest {

    @Mock
    private MainView mMainView;

    @Mock
    private ZaragozaAPIImplementation mAPIImplementation;

    private MainPresenterImplementation mMainPresenterImplementation;

    @Before
    public void setupClass() {
        MockitoAnnotations.initMocks(this);

        mMainPresenterImplementation = new MainPresenterImplementation(mMainView, mAPIImplementation);
    }

    @Test
    public void testLoadStationsDataSuccess() throws Exception {
        mMainPresenterImplementation.loadStationData();
        verify(mMainView).showProgress();
        verify(mAPIImplementation).getStations(mMainPresenterImplementation);
    }

    @Test
    public void testOnDataFailure() throws Exception {
        mMainPresenterImplementation.onDataFailure();
        verify(mMainView).hideProgress();
        verify(mMainView).showConnectionError();
    }

    @Test
    public void testOnDataAvailableWithEmptyData() throws Exception {
        List<Station> stations = null;
        mMainPresenterImplementation.onDataAvailable(stations);
        verify(mMainView).hideProgress();
    }

    @Test
    public void testOnDataAvailableWithData()  throws Exception {
        List<Station> stations = prepareList();
        mMainPresenterImplementation.onDataAvailable(stations);
        verify(mMainView).hideProgress();
        verify(mMainView).showApartmentList(stations);
    }

    private List<Station> prepareList() {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L ,"Zaragoza Station", "(731) C1", 41.63139435772203, -0.886143585091619, null));
        return stations;
    }




}