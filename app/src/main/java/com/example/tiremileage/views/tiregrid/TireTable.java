package com.example.tiremileage.views.tiregrid;

import android.os.Bundle;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.Repository;
import com.example.tiremileage.databinding.FragmentTireTableBinding;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;

import java.util.List;

public class TireTable extends Fragment {

    FragmentTireTableBinding binding;
    TireTableViewModel viewModel;
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(
                this).get(TireTableViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTireTableBinding.inflate(inflater, container, false);
        viewModel.allTires.observe(getViewLifecycleOwner(), tires -> {
            if (tires == null) {
                TextView textView = new TextView(this.getContext());
                textView.setText("Empty");
                binding.table.addView(textView);
            } else {
                for (Tire tire: tires){

                    TableRow tableRow = new TableRow(this.getContext());

                    TextView textView = new TextView(this.getContext());
                    textView.setText(String.valueOf(tire.id));
                    tableRow.addView(textView);

                    textView = new TextView(this.getContext());
                    textView.setText(" Company: "+tire.maker_name);
                    tableRow.addView(textView);

                    binding.table.addView(tableRow);
                }
            }
        });
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}