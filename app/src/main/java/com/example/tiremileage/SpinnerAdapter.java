package com.example.tiremileage;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tiremileage.room.Entities.Track;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    List<Track> tracks;
    public SpinnerAdapter(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position).vin;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(30,30,30,30);
        textView.setText(tracks.get(position).vin);
        return textView;
    }
}
