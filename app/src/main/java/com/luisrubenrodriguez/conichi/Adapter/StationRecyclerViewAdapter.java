package com.luisrubenrodriguez.conichi.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.luisrubenrodriguez.conichi.Model.Service.ZaragozaAPIImplementation;
import com.luisrubenrodriguez.conichi.Model.Station;
import com.luisrubenrodriguez.conichi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GamingMonster on 05.05.2017.
 * RecyclerView.Adapter for the Station List.
 */

public class StationRecyclerViewAdapter extends RecyclerView.Adapter<StationRecyclerViewAdapter.StationRecyclerViewHolder>
        implements ZaragozaAPIImplementation.OnBusDataAvailable {

    public static final String GOOGLE_API_URL_BASE = "http://maps.googleapis.com/maps/api/staticmap";
    private Context mContext;
    private List<Station> mStations;

    public StationRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public StationRecyclerViewAdapter.StationRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.station_item, parent, false);
        return new StationRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StationRecyclerViewAdapter.StationRecyclerViewHolder holder, int position) {
        Station station = mStations.get(position);
        holder.name.setText(station.getIdTitle());

        Uri builtUri = Uri.parse(GOOGLE_API_URL_BASE)
                .buildUpon()
                .appendQueryParameter("center", station.getLat().toString().concat(",").concat(station.getLon().toString()))
                .appendQueryParameter("zoom", "16")
                .appendQueryParameter("size", "200x120")
                .appendQueryParameter("sensor", "true")
                .build();

        //Fetch the map image
        Glide.with(mContext)
                .load(builtUri.toString())
                .asBitmap()
                .format(DecodeFormat.PREFER_RGB_565)
                .fitCenter()
                .into(holder.image);


        List<String> nextBuses = station.getNextBuses();
        //Fetch the next buses.
        if (nextBuses == null) {
            ZaragozaAPIImplementation.getInstance().getNextBuses(station.getId(), position, this);
            holder.next_bus.setText(mContext.getString(R.string.loading_next_bus));
        } else if (nextBuses.size() > 0) {
            holder.next_bus.setText(nextBuses.get(0));
        }

    }

    @Override
    public void onBusDataAvailable(List<String> nextBuses, int position) {
        if (mStations != null && mStations.size() > position) {
            mStations.get(position).setNextBuses(nextBuses);
            notifyItemChanged(position);
        }
    }

    @Override
    public void onBusDataError(int position) {
        if (mStations != null && mStations.size() > position) {
            List<String> noNextBusInfo = new ArrayList<>();
            noNextBusInfo.add(mContext.getString(R.string.error_next_bus));
            mStations.get(position).setNextBuses(noNextBusInfo);
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return ((mStations != null) && (mStations.size() != 0)) ? mStations.size() : 0;
    }

    public void loadNewData(List<Station> newStations) {
        mStations = newStations;
        notifyDataSetChanged();
    }

    static class StationRecyclerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.station_name)
        TextView name;
        @BindView(R.id.station_image)
        ImageView image;
        @BindView(R.id.next_bus)
        TextView next_bus;

        public StationRecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
