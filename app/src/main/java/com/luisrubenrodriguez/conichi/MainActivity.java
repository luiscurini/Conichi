package com.luisrubenrodriguez.conichi;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.luisrubenrodriguez.conichi.Adapter.StationRecyclerViewAdapter;
import com.luisrubenrodriguez.conichi.Model.Service.ZaragozaAPIImplementation;
import com.luisrubenrodriguez.conichi.Model.Station;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {

    //private static final String TAG = "MainActivity";
    public static final String STATIONDATA = "stationdata";
    public static final String LAYOUTDATA = "layoutdata";
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.station_list)
    RecyclerView mRecyclerView;
    private MainPresenterImplementation mPresenter;
    private LinearLayoutManager mRecyclerViewLayoutManager;
    private StationRecyclerViewAdapter mStationRecyclerViewAdapter;
    private List<Station> mStations;
    private Parcelable mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mPresenter = new MainPresenterImplementation(this, ZaragozaAPIImplementation.getInstance());
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mStations != null) {
            Parcelable state = mRecyclerViewLayoutManager.onSaveInstanceState();
            outState.putParcelable(LAYOUTDATA, state);
            outState.putParcelableArrayList(STATIONDATA, (ArrayList<? extends Parcelable>) mStations);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStations = savedInstanceState.getParcelableArrayList(STATIONDATA);
            mState = savedInstanceState.getParcelable(LAYOUTDATA);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == mStations) {
            mPresenter.loadStationData();
        } else {

            //Restores state.
            mStationRecyclerViewAdapter.loadNewData(mStations);
            if (mState != null) {
                mRecyclerViewLayoutManager.onRestoreInstanceState(mState);
            }
        }
        mState = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            showProgress();
            mPresenter.loadStationData();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows loading animation and hides the Chart
     */
    @Override
    public void showProgress() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    /**
     * Hides the loading animation
     */
    @Override
    public void hideProgress() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Takes the data received from the API call and informs the recyclerview.adapter of the change
     *
     * @param stations List of stations
     */
    @Override
    public void showApartmentList(List<Station> stations) {
        mStations = stations;
        if (mStations != null && mStations.size() > 0) {
            mStationRecyclerViewAdapter.loadNewData(mStations);
        }
    }

    @Override
    public void showConnectionError() {
        Toast.makeText(this, R.string.toast_error_message, Toast.LENGTH_LONG).show();
    }

    /**
     * Setup of the SwipeRefreshLayout.
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadStationData();
            }
        });
    }

    /**
     * Setup of the RecyclerView..
     */
    private void initRecyclerView() {
        mStationRecyclerViewAdapter = new StationRecyclerViewAdapter(this);
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this,
                mRecyclerViewLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerView.setAdapter(mStationRecyclerViewAdapter);
    }
}
