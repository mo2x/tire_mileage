package com.example.tiremileage.views.constructor.RecycleViewAdapters;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiremileage.databinding.RecyclerVinItemBinding;
import com.example.tiremileage.databinding.TireItemBinding;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Tire;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TireAdapter extends RecyclerView.Adapter<TireAdapter.TireViewHolder> {

    List<Tire> tires = new ArrayList<>();
    private OnClickListener onClickListener;
    @NonNull
    @Override
    public TireAdapter.TireViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TireItemBinding binding = TireItemBinding.inflate(layoutInflater,parent,false);
        return new TireViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TireAdapter.TireViewHolder holder, int position) {
        Tire tire = tires.get(position);
        holder.tireItemBinding.tireSize.setText(tire.tSize);
        holder.tireItemBinding.tireThread.setText(String.valueOf(tire.treadDepth));
        holder.tireItemBinding.serialNumber.setText(tire.serialNumber);
        holder.tireItemBinding.getRoot().setVisibility(View.VISIBLE);
        Context context = holder.tireItemBinding.getRoot().getContext();
        int id = context.getResources().getIdentifier(tire.pic.replace(".png",""),"drawable", context.getPackageName());
        holder.tireItemBinding.imageView.setImageResource(id);
        holder.tireItemBinding.getRoot().setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(String.valueOf(tire.id));
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

            ClipData dragData = new ClipData("tire", mimeTypes, item);
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.tireItemBinding.imageView);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(dragData, myShadow, v, 0);
            }
            v.setVisibility(View.INVISIBLE);
            return true;
        });
        holder.tireItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null){
                    onClickListener.onClick(tires.get(holder.getAdapterPosition()));
                }
            }
        });
    }
    public void setOnClickListener(OnClickListener onClickListener){
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
        TireItemBinding tireItemBinding;
        public TireViewHolder(TireItemBinding tireItemBinding){
            super(tireItemBinding.getRoot());
            this.tireItemBinding = tireItemBinding;
        }
    }

    public void setTires(List<Tire> tires){
        this.tires = tires;
    }
}
