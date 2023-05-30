package com.example.tiremileage.views.constructor;


import android.annotation.SuppressLint;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiremileage.Repository;
import com.example.tiremileage.customItems.SpinnerAdapter;

import com.example.tiremileage.customItems.Connector;
import com.example.tiremileage.customItems.TireItem;
import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.TrackWithValidTires;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static androidx.core.content.ContextCompat.getDrawable;

public class Constructor extends Fragment {

    FragmentConstructorBinding binding;
    ConstructorViewModel viewModel;
    boolean isIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(
                requireActivity()).get(ConstructorViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConstructorBinding.inflate(inflater, container, false);

        binding.horizontalScrollView.setOnDragListener((v, event) -> {
            View dragView =(View) event.getLocalState();
            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                case DragEvent.ACTION_DRAG_EXITED :
                    isIn = false;
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    isIn = true;
                    break;
                case DragEvent.ACTION_DRAG_ENDED   :
                    if (!event.getResult())
                        if (dragView.getVisibility() == INVISIBLE) {
                            dragView.post(() -> dragView.setVisibility(VISIBLE));
                        }
                    break;
                case DragEvent.ACTION_DROP:
                    if (!isIn) {
                        return false;
                    }
                    if (event.getClipDescription() == null){
                        return true;
                    }
                    if (!event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        return false;
                    }
                    if (!event.getClipDescription().getLabel().toString().equals("tire")) {
                        return false;
                    }
                    CharSequence draggedData = event.getClipData().getItemAt(0).getText();
                    int tireID = Integer.parseInt(draggedData.toString());
                    Repository repository = new Repository();
                    repository.updateTirePosByID(requireContext(),tireID, "0");
                    break;
                default: break;
            }
            return true;
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.allTracks.observe(getViewLifecycleOwner(), tracks ->
                binding.spinner.setAdapter(new SpinnerAdapter(tracks)));

        viewModel.tracksWithValidTires.observe(getViewLifecycleOwner(), trackWithValidTires -> {
            if (viewModel.currentSpinPos.getValue() != null)
                resetTireItems(trackWithValidTires.get(viewModel.currentSpinPos.getValue()));
        });

        viewModel.currentSpinPos.observe(getViewLifecycleOwner(), integer -> {
            binding.FL.removeAllViews();
            if (viewModel.tracksWithValidTires.getValue() != null) {
                List<TrackWithValidTires> tracksWithValidTires =
                        Objects.requireNonNull(viewModel.tracksWithValidTires.getValue());
                TrackWithValidTires trackWithValidTires = tracksWithValidTires.get(integer);
                String model = trackWithValidTires.track.model;
                @SuppressLint("DiscouragedApi") int modelLayout = getResources().
                        getIdentifier("l" + model, "layout", requireActivity().getPackageName());
                getLayoutInflater().inflate(modelLayout, binding.FL);
                resetTireItems(trackWithValidTires);
            } else {
                System.out.printf("null");
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.currentSpinPos.setValue(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void resetTireItems(TrackWithValidTires trackWithValidTires) {
        binding.scrollLayout.removeAllViews();
        ConstraintLayout connectorLay = (ConstraintLayout) binding.FL.getChildAt(1);
        if (connectorLay == null)
            return;
        for (int i = 0; i < connectorLay.getChildCount(); i++) {
            Connector connector = (Connector) connectorLay.getChildAt(i);
            connector.setImageDrawable(null);
            connector.setVisibility(VISIBLE);
        }
        for (Tire tire : trackWithValidTires.tires) {
            if (Objects.equals(tire.pos, "0")) {
                TireItem tireItem = new TireItem(getContext());
                tireItem.setTire(tire);
                binding.scrollLayout.addView(tireItem);
            } else {
                for (int i = 0; i < connectorLay.getChildCount(); i++) {
                    Connector connector = (Connector) connectorLay.getChildAt(i);
                    if (connector.getPosition() == Integer.parseInt(tire.pos)) {
                        String pic = tire.pic.replaceAll(".png", "");
                        @SuppressLint("DiscouragedApi") int picID =
                                getResources().getIdentifier(pic, "drawable", requireActivity().getPackageName());
                        Drawable drawable = getDrawable(requireContext(), picID);
                        connector.setImageDrawable(drawable);
                        connector.setID(tire.id);
                    }
                }
            }
        }
    }
}