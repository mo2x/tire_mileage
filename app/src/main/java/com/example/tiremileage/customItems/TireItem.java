package com.example.tiremileage.customItems;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.example.tiremileage.R;
import com.example.tiremileage.room.Entities.Tire;

public class TireItem extends LinearLayout {

    ImageView imageView;
    int id;
    Tire tire;
    boolean isIn;

    TextView serNum;
    TextView millage;
    TextView pressure;
    public TireItem(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TireItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TireItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        String msg = "";
        LayoutInflater.from(context).inflate(R.layout.tire_item, this, true);
        imageView = findViewById(R.id.imageView);
        serNum = findViewById(R.id.serial_number);
        millage = findViewById(R.id.mileage);
        pressure = findViewById(R.id.pressure);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item(String.valueOf(tire.id));
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData("tire",mimeTypes, item);
                DragShadowBuilder myShadow = new DragShadowBuilder(imageView);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(dragData, myShadow, v, 0);
                    v.setVisibility(INVISIBLE);
                }
                return true;
            }
        });

        setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                View dragView = (View) event.getLocalState();
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        isIn = false;
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        isIn = true;
                        break;

                    case DragEvent.ACTION_DRAG_EXITED :
                        isIn = false;
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :

                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :
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
                    default: break;
                }
                return true;
            }
        });

    }

    public void setTire(Tire tire){
        this.tire = tire;
        String pic = tire.pic.replaceAll(".png","");
        imageView.setImageResource(getResources().getIdentifier(pic,"drawable",getContext().getPackageName()));
        serNum.setText(tire.serialNumber);
        millage.setText(String.valueOf(tire.km));
        pressure.setText(tire.kpa);
    }
}
