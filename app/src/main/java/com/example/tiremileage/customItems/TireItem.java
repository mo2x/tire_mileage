package com.example.tiremileage.customItems;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.tiremileage.R;
import com.example.tiremileage.room.Entities.Tire;

public class TireItem extends LinearLayout {

    ImageView imageView;
    Tire tire;
    TextView serNum;
    TextView millage;
    TextView pressure;
    public TireItem(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tire_item, this, true);
        imageView = findViewById(R.id.imageView);
        serNum = findViewById(R.id.serial_number);
        millage = findViewById(R.id.mileage);
        pressure = findViewById(R.id.pressure);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(String.valueOf(tire.id));
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

            ClipData dragData = new ClipData("tire", mimeTypes, item);
            DragShadowBuilder myShadow = new DragShadowBuilder(imageView);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(dragData, myShadow, v, 0);
            }
            v.setVisibility(INVISIBLE);
            return true;
        });

    }
    @SuppressLint("DiscouragedApi")
    public void setTire(Tire tire){
        this.tire = tire;
        String pic = tire.pic.replaceAll(".png","");
        imageView.setImageResource(getResources().getIdentifier(pic,"drawable",getContext().getPackageName()));
        serNum.setText(tire.serialNumber);
        millage.setText(String.valueOf(tire.km));
        pressure.setText(String.valueOf(tire.kpa));
    }
}
