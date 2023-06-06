package com.example.tiremileage.views.about_program;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiremileage.LoginActivity;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.databinding.FragmentAboutProgramBinding;
import com.example.tiremileage.repository.RepositoryManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static androidx.databinding.DataBindingUtil.setContentView;

public class AboutProgram extends Fragment {
    FragmentAboutProgramBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutProgramBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepositoryManager.getRepository().clearSession();
                Intent intent = new Intent(RepositoryManager.getRepository().application.getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return binding.getRoot();
    }
}