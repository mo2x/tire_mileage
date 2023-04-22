package com.example.tiremileage.customItems;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class Connector extends androidx.appcompat.widget.AppCompatImageView {

    private int position;
    public Connector(Context context) {
        super(context);
        init(null, 0);
    }

    public Connector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Connector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int n) {
        setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                boolean isIn = false;
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        break;

                    case DragEvent.ACTION_DRAG_EXITED :

                        break;

                    case DragEvent.ACTION_DRAG_LOCATION  :

                        break;

                    case DragEvent.ACTION_DRAG_ENDED   :

                        break;

                    case DragEvent.ACTION_DROP:
                            event.getResult();
                        break;
                    default: break;
                }
                return true;
            }
        });
    }

}
