package com.example.tiremileage.customItems;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import org.jetbrains.annotations.Nullable;
import com.example.tiremileage.R;
import com.example.tiremileage.Repository;
public class Connector extends androidx.appcompat.widget.AppCompatImageView {

    boolean isIn = false;
    private int position;
    private int tireID = -1;
    public Connector(Context context) {
        super(context);
        init(context,null, 0);
    }

    public Connector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public Connector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int n) {


        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Connector, n, 0);
        position = attributes.getInt(R.styleable.Connector_position, n);
        attributes.recycle();
        setOnLongClickListener(v -> {
            if (getDrawable() == null || tireID == -1)
                return false;
            ClipData.Item item = new ClipData.Item(String.valueOf(tireID));
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

            ClipData dragData = new ClipData("tire",mimeTypes, item);

            Drawable back = getBackground();
            setBackground(null);

            DragShadowBuilder myShadow = new DragShadowBuilder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.setVisibility(INVISIBLE);
                v.startDragAndDrop(dragData, myShadow, v, 0);
                setBackground(back);

            }
            return true;
        });
        setOnDragListener((v, event) -> {
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
                    if (getDrawable() != null){
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
                    tireID = Integer.parseInt(draggedData.toString());
                    Repository repository = new Repository();
                    repository.updateTirePosByID(context,tireID,String.valueOf(position));
                    break;

                default: break;
            }

            return true;


        });
    }
    public void setID(int id){
        tireID = id;
    }
    public int getPosition() {
        return position;
    }
}
