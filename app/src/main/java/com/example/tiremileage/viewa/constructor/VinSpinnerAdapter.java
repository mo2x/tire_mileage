package com.example.tiremileage.viewa.constructor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import com.example.tiremileage.databinding.VinItemBinding;
import com.example.tiremileage.room.Entities.Track;

import java.util.ArrayList;
import java.util.List;

public class VinSpinnerAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    private List<Track> list;
    private List<Track> listFiltered;

    public VinSpinnerAdapter(Context context, List<Track> list)
    {
        this.context = context;
        this.list = list;
        listFiltered = list;
    }

    public void setList(List<Track> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return listFiltered != null ? listFiltered.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.example.tiremileage.databinding.VinItemBinding binding = VinItemBinding.inflate(LayoutInflater.from(context), parent, false);
        binding.textView.setText(listFiltered.get(position).vin);
        return binding.getRoot();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return resultValue.toString();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0) {
                    filterResults.count = list.size();
                    filterResults.values = list;
                }else {
                    List<Track> resultsList = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    for(Track track:list){
                        if(track.vin.indexOf(searchStr) == 0){
                            resultsList.add(track);
                        }
                        filterResults.count = resultsList.size();
                        filterResults.values = resultsList;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(@NonNull CharSequence constraint, FilterResults results) {
                listFiltered = (List<Track>) results.values;
                notifyDataSetChanged();
            }

        };
    }
}
