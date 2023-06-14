package com.example.tiremileage.views.constructor.RecycleViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiremileage.databinding.RecyclerVinItemBinding;
import com.example.tiremileage.room.Entities.Car;

import java.util.ArrayList;
import java.util.List;

public class VinAdapter extends RecyclerView.Adapter<VinAdapter.VinViewHolder> {

    private List<Car> cars = new ArrayList<>();
    private OnClickListener onClickListener;
    @NonNull
    @Override
    public VinAdapter.VinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerVinItemBinding binding = RecyclerVinItemBinding.inflate(layoutInflater,parent,false);
        return new VinViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VinAdapter.VinViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.recyclerVinItemBinding.vinTextView.setText(cars.get(position).vin);
        Context context = holder.recyclerVinItemBinding.getRoot().getContext();
        int id = context.getResources().getIdentifier("v"+car.model,"drawable", context.getPackageName());
        holder.recyclerVinItemBinding.vinImageView.setImageResource(id);
        holder.recyclerVinItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null ){
                    onClickListener.onClick(cars.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(Car car);
    }

    class VinViewHolder extends RecyclerView.ViewHolder{
        RecyclerVinItemBinding recyclerVinItemBinding;
        public VinViewHolder(RecyclerVinItemBinding recyclerVinItemBinding){
            super(recyclerVinItemBinding.getRoot());
            this.recyclerVinItemBinding = recyclerVinItemBinding;
        }
    }

    public void addCar(){
        Car car = new Car();
        car.vin = String.valueOf(getItemCount()+1);
        cars.add(car);
    }
    public void setCars(List<Car> cars){
        this.cars = cars;
    }
}
