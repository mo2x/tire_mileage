package com.example.tiremileage.viewa.constructor;

import android.os.Bundle;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.VinCorp;
import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Track;
import org.jetbrains.annotations.NotNull;

public class Constructor extends Fragment {

    FragmentConstructorBinding binding;
    VinSpinnerAdapter adapter;
    ConstructorViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(
                this).get(ConstructorViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConstructorBinding.inflate(inflater, container, false);
        initAutoCompleteTextView();
        viewModel.allTracks.observe(getViewLifecycleOwner(), tracks -> {
            adapter.setList(tracks);
        });
        return binding.getRoot();
    }

    private void initAutoCompleteTextView()
    {
        adapter = new VinSpinnerAdapter(binding.constructor.getContext(), viewModel.allTracks.getValue());
        binding.autoCompleteTextView.setAdapter(adapter);
        binding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.autoCompleteTextView.setText(((Track)adapter.getItem(position)).vin);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}