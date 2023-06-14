package com.example.tiremileage.views.tiregrid.RecycleViewAdapter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiremileage.databinding.TireItemBinding;
import com.example.tiremileage.databinding.TireListItemBinding;
import com.example.tiremileage.room.Entities.Tire;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TireListAdapter  extends RecyclerView.Adapter<TireListAdapter.TireViewHolder> {

    List<Tire> tires = new ArrayList<>();
    private TireListAdapter.OnClickListener onClickListener;
    @NonNull
    @Override
    public TireListAdapter.TireViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TireListItemBinding binding = TireListItemBinding.inflate(layoutInflater,parent,false);
        return new TireViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TireViewHolder holder, int position) {
        Tire tire = tires.get(position);
        holder.tireItemBinding.idHolder.setText(String.valueOf(tire.id));
        holder.tireItemBinding.serialNumberHolder.setText(tire.serialNumber);
        holder.tireItemBinding.millageHolder.setText(String.valueOf(tire.km));
        holder.tireItemBinding.positionHolder.setText(tire.pos);
        holder.tireItemBinding.pressureHolder.setText(tire.p_kph + "/" + tire.kpa);
        holder.tireItemBinding.temperatureHolder.setText(String.valueOf(tire.tepm));
        holder.tireItemBinding.makerNameHolder.setText(tire.maker_name);
        holder.tireItemBinding.vinHolder.setText(tire.vin);
        holder.tireItemBinding.tireChamderHolder.setText(tire.tSize);
        holder.tireItemBinding.getRoot().setVisibility(View.VISIBLE);
        holder.tireItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null){
                    onClickListener.onClick(tires.get(holder.getAdapterPosition()));
                }
            }
        });
    }
    public void setOnClickListener(TireListAdapter.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(Tire tire);
    }

    @Override
    public int getItemCount() {
        return tires.size();
    }
    class TireViewHolder extends RecyclerView.ViewHolder{
        TireListItemBinding tireItemBinding;
        public TireViewHolder(TireListItemBinding tireItemBinding){
            super(tireItemBinding.getRoot());
            this.tireItemBinding = tireItemBinding;
        }
    }

    public void setTires(List<Tire> tires){
        this.tires = tires;
    }
}
