package com.example.tiremileage.customItems;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.example.tiremileage.room.Entities.Car;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    List<Car> cars;
    public SpinnerAdapter(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public int getCount() {
        return cars.size();
    }

    @Override
    public Object getItem(int position) {
        return cars.get(position).vin;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(30,30,30,30);
        textView.setText(cars.get(position - 1).vin);
        return textView;
    }
}
