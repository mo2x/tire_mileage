package com.example.tiremileage.models;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tiremileage.BuildConfig;
import com.example.tiremileage.R;
import com.example.tiremileage.viewa.constructor.ICallBack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CFragmentHub extends Fragment {

    ICallBack iCallBack;
    String res;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Bundle  bundle = this.getArguments();
        return inflater.inflate(getResources().getIdentifier(bundle.getString("Res"), "layout", requireActivity().getPackageName()), null);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (!(context instanceof ICallBack)) {
            throw new ClassCastException("Activity must implement fragment's ICallbacks.");
        }
        iCallBack = (ICallBack) context;
        res = iCallBack.getString();
    }
}
