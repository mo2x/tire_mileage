package com.example.tiremileage.views.tiregrid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.databinding.FragmentTireTableBinding;
import com.example.tiremileage.room.Entities.Tire;

import java.util.Arrays;
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
            binding.tireTable.clearData();
            if (tires != null) {
                for (Tire tire:tires) {
                    List<String> raw = Arrays.asList(
                            tire.serialNumber,
                            String.valueOf(tire.km),
                            tire.tSize,
                            String.valueOf(tire.treadDepth),
                            String.valueOf(tire.tkph),
                            String.valueOf(tire.kpa),
                            String.valueOf(tire.p_kph),
                            tire.maker_name,
                            tire.vin,
                            tire.pos
                    );
                    binding.tireTable.addRaw(raw);
                }
            }
        });
        return binding.getRoot();
    }
    /*
<string-array name="Tires">
    <item>Serial number</item>
    <item>Mileage</item>
    <item>Tire chamber</item>
    <item>Tread Depth</item>
    <item>Productivity</item>
    <item>Current pressure</item>
    <item>Nominal pressure</item>
    <item>Maker Name</item>
</string-array>
*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}