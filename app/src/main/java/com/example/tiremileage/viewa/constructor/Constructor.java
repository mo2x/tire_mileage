package com.example.tiremileage.viewa.constructor;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.R;
import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.models.CFragmentHub;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import org.jetbrains.annotations.NotNull;

public class Constructor extends Fragment {

    FragmentConstructorBinding binding;
    VinSpinnerAdapter adapter;
    ConstructorViewModel viewModel;
    String activeModel;
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
        initAutoCompleteTextView();
        viewModel.allTires.observe(getViewLifecycleOwner(), tires -> {
            for (Tire tire: tires) {
                pic = tire.pic.replaceAll(".png","");
                resID = getResources().getIdentifier(pic, "drawable",
                        getContext().getApplicationContext().getPackageName() );
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(resID);
                binding.scrollLayout.addView(imageView);
            }
        });
        viewModel.allTracks.observe(getViewLifecycleOwner(), tracks -> {
            adapter.setList(tracks);
        });


        return binding.getRoot();
    }

    public void initAutoCompleteTextView()
    {
        adapter = new VinSpinnerAdapter(binding.constructor.getContext(), viewModel.allTracks.getValue());
        binding.autoCompleteTextView.setAdapter(adapter);
        binding.autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.autoCompleteTextView.showDropDown();
            }
        });
        binding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = (Track) adapter.getItem(position);
                binding.autoCompleteTextView.setText(track.vin);
                activeModel = track.model;
                CFragmentHub fragmentHub = new CFragmentHub();
                Bundle bundle = new Bundle();
                bundle.putString("Res", track.model);
                fragmentHub.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.FL, fragmentHub);
                transaction.commit();
            }
        });
    }

    private void changeModel(String model)
    {
        int resID = getResources().getIdentifier(model, "layout",
                getContext().getApplicationContext().getPackageName());
        if (resID == 0)
            return;
    }
}