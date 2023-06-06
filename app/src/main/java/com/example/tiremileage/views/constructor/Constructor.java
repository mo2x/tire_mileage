package com.example.tiremileage.views.constructor;


import android.app.Dialog;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiremileage.R;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.databinding.DialogSearchableSpinnerBinding;

import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.views.constructor.RecycleViewVin.VinAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.tiremileage.customItems.Status.*;

public class Constructor extends Fragment {

    FragmentConstructorBinding binding;
    ConstructorViewModel viewModel;
    boolean isIn;
    Dialog dialog;

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

        viewModel.currentVin.observe(getViewLifecycleOwner(), s -> {
            if (s == null || s.equals("")) {
                binding.vinButton.setText("VIN don't set");
            } else {
                binding.vinButton.setText("VIN: "+s);
            }
        });

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
                    RepositoryManager.getRepository().updateTirePosByID(requireContext(),tireID, "0");
                    break;
                default: break;
            }
            return true;
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.vinButton.setOnClickListener(v -> {
            dialog = new Dialog(getContext());
            DialogSearchableSpinnerBinding dialogBinding = DialogSearchableSpinnerBinding.inflate(dialog.getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());

            LinearLayoutManager manager = new LinearLayoutManager(dialog.getContext());
            VinAdapter vinAdapter = new VinAdapter();

            vinAdapter.setOnClickListener(car -> {
                viewModel.currentVin.postValue(car.vin);
                dialog.dismiss();
            });

            dialogBinding.editTextTextPersonName2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    viewModel.clearCars();
                    viewModel.setStatus(IS_LOADING);
                }
                @Override
                public void afterTextChanged(Editable s) {
                    viewModel.loadPoolObj(dialogBinding.editTextTextPersonName2.getText().toString());
                }
            });

            dialogBinding.vinRecView.setLayoutManager(manager);
            viewModel.status.observe(getViewLifecycleOwner(), status -> {
                switch (status){
                    case ALL_LOADED:
                    case LOADED:
                        dialogBinding.statusTextView.setVisibility(INVISIBLE);
                        dialogBinding.progressBar2.setVisibility(INVISIBLE);
                        dialogBinding.button2.setVisibility(INVISIBLE);
                        break;
                    case NO_INTERNET:
                        if (viewModel.carPool.getValue().size()==0){
                            dialogBinding.statusTextView.setText(R.string.no_internet);
                            dialogBinding.statusTextView.setVisibility(VISIBLE);
                            dialogBinding.button2.setVisibility(VISIBLE);
                            dialogBinding.progressBar2.setVisibility(INVISIBLE);
                        } else {
                            dialogBinding.statusTextView.setVisibility(INVISIBLE);
                            dialogBinding.button2.setVisibility(INVISIBLE);
                            dialogBinding.progressBar2.setVisibility(INVISIBLE);
                            Toast toast = new Toast(getContext());
                            toast.setText(R.string.no_internet);
                            toast.show();
                        }
                        break;
                    case IS_LOADING:
                        dialogBinding.statusTextView.setVisibility(INVISIBLE);
                        dialogBinding.progressBar2.setVisibility(VISIBLE);
                        dialogBinding.button2.setVisibility(INVISIBLE);
                        break;
                    case NO_SERVER_RESPONSE:
                        if (viewModel.carPool.getValue().size()==0){
                            dialogBinding.statusTextView.setText(R.string.no_server_response);
                            dialogBinding.statusTextView.setVisibility(VISIBLE);
                            dialogBinding.button2.setVisibility(VISIBLE);
                            dialogBinding.progressBar2.setVisibility(INVISIBLE);
                        } else {
                            dialogBinding.statusTextView.setVisibility(INVISIBLE);
                            dialogBinding.button2.setVisibility(INVISIBLE);
                            dialogBinding.progressBar2.setVisibility(INVISIBLE);
                            Toast toast = new Toast(getContext());
                            toast.setText(R.string.no_server_response);
                            toast.show();
                        }
                        break;
                }
            });
            viewModel.carPool.observe(getViewLifecycleOwner(), cars -> {
                dialogBinding.progressBar2.setVisibility(INVISIBLE);
                dialogBinding.button2.setVisibility(INVISIBLE);
                dialogBinding.statusTextView.setVisibility(INVISIBLE);
                vinAdapter.setCars(cars);
                dialogBinding.vinRecView.setAdapter(vinAdapter);
            });

            viewModel.loadPoolObj(dialogBinding.editTextTextPersonName2.getText().toString());

            dialogBinding.vinRecView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = manager.getChildCount();
                    int totalCount = manager.getItemCount();
                    int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                    if (visibleItemCount + firstVisibleItemPosition >= totalCount
                            && firstVisibleItemPosition >= 0){
                        viewModel.loadPoolObj(dialogBinding.editTextTextPersonName2.getText().toString());
                    }
                }
            });

            dialog.getWindow().setLayout(-1,800);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            dialog.setOnDismissListener(dialog -> {
                viewModel.setStatus(Status.IS_LOADING);
                viewModel.carPool.postValue(new ArrayList<>());
            });

            dialog.show();
        });
    }
}