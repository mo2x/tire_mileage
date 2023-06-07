package com.example.tiremileage.views.constructor;


import android.app.Dialog;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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

import com.example.tiremileage.databinding.EmptyBinding;
import com.example.tiremileage.databinding.FragmentConstructorBinding;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Model;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.views.constructor.RecycleViewVin.VinAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.tiremileage.customItems.Status.*;

public class Constructor extends Fragment {

    EmptyBinding emptyBinding;
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
        viewModel.model.observe(getViewLifecycleOwner(), model -> {
            if (model==null){
                if (binding.FL.getChildCount()>0){
                    ViewGroup view = (ViewGroup) binding.FL.getChildAt(1);
                    view.removeAllViews();
                }
                binding.FL.removeAllViews();
            } else {
                binding.FL.removeAllViews();
                emptyBinding = EmptyBinding.inflate(inflater, binding.FL);
                emptyBinding.connectorLayout.removeAllViews();
                ViewGroup viewGroup = (ViewGroup) model.connectors[0].getParent();
                if (viewGroup != null)
                    viewGroup.removeAllViews();
                for (int i = 0; i< model.connectors.length; i++){
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0,0);
                    if (model.connectors[i].getTire() == null)
                        model.connectors[i].setBackground(ContextCompat.getDrawable(getContext(),R.drawable.connector));
                        else
                    {
                        int drawID = getResources().getIdentifier
                                (model.connectors[i].getTire().pic.replace(".png",""),
                                        "drawable",
                                        getContext().getPackageName());
                        Drawable drawable = getResources().getDrawable(drawID);
                        model.connectors[i].setBackground(drawable);
                    }
                    emptyBinding.constructorImageView.setImageResource(getResources().getIdentifier(
                            "m"+model.modelSn,"drawable",getContext().getPackageName()
                    ));
                    params.bottomToBottom = emptyBinding.connectorLayout.getId();
                    params.startToStart = emptyBinding.connectorLayout.getId();
                    params.topToTop = emptyBinding.connectorLayout.getId();
                    params.endToEnd = emptyBinding.connectorLayout.getId();
                    params.horizontalBias = (float) (model.connectors[i].getLeftDouble());
                    params.verticalBias = (float) (model.connectors[i].getTopDouble());
                    params.matchConstraintPercentWidth = (float) model.connectors[i].getWidthP();
                    params.matchConstraintPercentHeight = (float) model.connectors[i].getHeightP();
                    model.connectors[i].setLayoutParams(params);
                    emptyBinding.connectorLayout.addView(model.connectors[i]);
                }
            }
        });
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
                    //RepositoryManager.getRepository().updateTirePosByID(requireContext(),tireID, "0");
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
                viewModel.getModel(car.model);
                dialog.dismiss();
            });

            dialogBinding.editTextTextPersonName2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    viewModel.clearCars();
                }
                @Override
                public void afterTextChanged(Editable s) {
                    viewModel.setRecyclerViewStatus(IS_LOADING);
                    viewModel.loadPoolObj(dialogBinding.editTextTextPersonName2.getText().toString());
                }
            });

            dialogBinding.vinRecView.setLayoutManager(manager);
            viewModel.recyclerViewStatus.observe(getViewLifecycleOwner(), status -> {
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
                viewModel.postStatus(Status.IS_LOADING);
                viewModel.carPool.setValue(new ArrayList<>());
            });

            dialog.show();
        });
    }
}