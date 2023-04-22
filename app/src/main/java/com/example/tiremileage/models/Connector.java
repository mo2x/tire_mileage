package com.example.tiremileage.models;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

public class Connector extends androidx.appcompat.widget.AppCompatImageView {

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
        setOnDragListener((v, event) -> false);
    }
}
