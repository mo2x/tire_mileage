package com.example.tiremileage.views.constructor;

import android.content.ClipDescription;
import android.os.Bundle;
import android.view.DragEvent;
import android.widget.AdapterView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiremileage.R;
import com.example.tiremileage.Repository;
import com.example.tiremileage.SpinnerAdapter;
import com.example.tiremileage.customItems.CFragmentHub;
import com.example.tiremileage.customItems.Connector;
import com.example.tiremileage.customItems.TireItem;
import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.room.Entities.Tire;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.view.View.VISIBLE;
import static androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable;

public class Constructor extends Fragment {

    FragmentConstructorBinding binding;
    ConstructorViewModel viewModel;
    boolean isIn = false;

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
                if (Objects.equals(tire.vin, "-")) {
                    TireItem tireItem = new TireItem(getContext());
                    tireItem.setTire(tire);
                    binding.scrollLayout.addView(tireItem);
                }
            }
            //viewModel.allTires.removeObservers(getViewLifecycleOwner());
        });
        viewModel.allTracks.observe(getViewLifecycleOwner(), tracks -> {
                binding.spinner.setAdapter(new SpinnerAdapter(tracks));
        });
        binding.horizontalScrollView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                View dragView = (View) event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        isIn = false;
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        isIn = true;
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        isIn = false;
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:

                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        if (!event.getResult())
                            dragView.setVisibility(VISIBLE);

                        break;

                    case DragEvent.ACTION_DROP:
                        if (isIn) {
                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                if (event.getClipDescription().getLabel().toString().equals("CTire")) {
                                    Connector item = (Connector) dragView;
                                    item.returnTire();
                                    item.setImageDrawable(null);
                                    item.setVisibility(VISIBLE);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new CFragmentHub();
                Bundle bundle = new Bundle();
                String model = viewModel.allTracks.getValue().get(position).model;
                viewModel.postCurrentTrack(viewModel.allTracks.getValue().get(position));
                bundle.putString("Res", model);
                bundle.putString("vin", viewModel.allTracks.getValue().get(position).vin);
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