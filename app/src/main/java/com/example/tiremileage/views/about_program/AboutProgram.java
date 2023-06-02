package com.example.tiremileage.views.about_program;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tiremileage.LoginActivity;
import com.example.tiremileage.R;
import com.example.tiremileage.Repository;
import com.example.tiremileage.databinding.FragmentAboutProgramBinding;
import com.example.tiremileage.databinding.FragmentConstructorBinding;

import static androidx.databinding.DataBindingUtil.setContentView;

public class AboutProgram extends Fragment {
    FragmentAboutProgramBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutProgramBinding.inflate(inflater, container, false);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repository.clearSession();
                Intent intent = new Intent(Repository.application.getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}