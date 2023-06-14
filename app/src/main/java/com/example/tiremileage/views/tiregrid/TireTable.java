package com.example.tiremileage.views.tiregrid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.tiremileage.databinding.FragmentTireTableBinding;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.views.constructor.RecycleViewAdapters.TireAdapter;
import com.example.tiremileage.views.tiregrid.RecycleViewAdapter.TireListAdapter;

import java.util.Arrays;
import java.util.List;

public class TireTable extends Fragment {

    FragmentTireTableBinding binding;
    TireTableViewModel viewModel;
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity()).get(TireTableViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTireTableBinding.inflate(inflater, container, false);
        TireListAdapter tireAdapter = new TireListAdapter();
        viewModel.updateTirePool();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.tiresRecycler.setLayoutManager(linearLayoutManager);
        binding.tiresRecycler.setAdapter(tireAdapter);
        tireAdapter.setOnClickListener(tire -> viewModel.setCurrentTire(tire));
        viewModel.tirePool.observe(getViewLifecycleOwner(), tires -> {
            tireAdapter.setTires(tires);
            tireAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}