package com.example.tiremileage.customItems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.Entities.Tire;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CFragmentHub extends Fragment {
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
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout constraintLayout = (ConstraintLayout) view;
        constraintLayout = (ConstraintLayout) constraintLayout.getChildAt(1);
        for (int i = 0; i < constraintLayout.getChildCount(); i++){
            Connector connector = (Connector) constraintLayout.getChildAt(i);
            CFThread thread = new CFThread(connector);
            thread.start();
        }

    }
    public class CFThread extends Thread{
        Connector connector;
        public CFThread(Connector connector){
            this.connector = connector;
        }

        @Override
        public void run() {
            Tire tire = (new Repository()).getTireByPos(getContext(), String.valueOf(connector.position));
            if (tire!= null){
                Bundle bundle = getArguments();
                if(Objects.equals(tire.vin, bundle.getString("vin"))) {
                    String pic = tire.pic.replaceAll(".png", "");
                    if (connector.imageView == null){
                        connector.imageView = new ImageView(getContext());
                    }
                    connector.imageView.setImageResource(getResources().getIdentifier(pic, "drawable", getContext().getPackageName()));
                    connector.setImageDrawable(connector.imageView.getDrawable());
                    connector.tireID = tire.id;
                }
            }
        }
    }
}
