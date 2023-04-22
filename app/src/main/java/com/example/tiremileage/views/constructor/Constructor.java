package com.example.tiremileage.views.constructor;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.R;
import com.example.tiremileage.SpinnerAdapter;
import com.example.tiremileage.customItems.CFragmentHub;
import com.example.tiremileage.customItems.TireItem;
import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import org.jetbrains.annotations.NotNull;

public class Constructor extends Fragment {

    FragmentConstructorBinding binding;
    ConstructorViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(
                this).get(ConstructorViewModel.class);

    }

    int resID;
    String pic;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentConstructorBinding.inflate(inflater, container, false);
        viewModel.allTires.observe(getViewLifecycleOwner(), tires -> {
            binding.scrollLayout.removeAllViews();
            for (Tire tire: tires) {
                TireItem tireItem = new TireItem(getContext());
                tireItem.setTire(tire);
                binding.scrollLayout.addView(tireItem);
            }
            viewModel.allTires.removeObservers(getViewLifecycleOwner());
        });
        viewModel.allTracks.observe(getViewLifecycleOwner(), tracks -> {
                binding.spinner.setAdapter(new SpinnerAdapter(tracks));
        });
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new CFragmentHub();
                Bundle bundle = new Bundle();
                String model = viewModel.allTracks.getValue().get(position).model;
                bundle.putString("Res", model);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

// Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.FL, fragment);
                transaction.addToBackStack(null);
// Commit the transaction
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }


}