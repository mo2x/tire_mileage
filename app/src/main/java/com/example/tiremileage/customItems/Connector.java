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
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Tire;
import org.jetbrains.annotations.Nullable;
import com.example.tiremileage.R;

public class Connector extends androidx.appcompat.widget.AppCompatImageView {

    Tire tire;
    boolean isIn = false;
    private int position;
    private String tireSize;
    private double left;
    private double top;
    private double heightP;
    private double widthP;
    private String parentVin;
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
        setOnClickListener(v -> {
            Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
            toast.setText("Id:"+ position +";   Tire size:"+tireSize);
            toast.show();
        });
        setOnLongClickListener(v -> {
            if (tire == null)
                return false;
            ClipData.Item item = new ClipData.Item(String.valueOf(tire.id));
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
                    RepositoryManager.getRepository().postTire(draggedData.toString(), getPosition(), parentVin);

                    //RepositoryManager.getRepository().updateTirePosByID(context,tireID,String.valueOf(position));
                    break;

                default: break;
            }

            return true;


        });
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int pos){position = pos;}

    public String getTireSizeDouble() {
        return tireSize;
    }

    public void setTireSize(String tireSize) {
        this.tireSize = tireSize;
    }

    public double getLeftDouble() {
        return left;
    }

    public void setLeftDouble(double left) {
        this.left = left;
    }

    public double getTopDouble() {
        return top;
    }

    public void setTopDouble(double top) {
        this.top = top;
    }
    public void setHeightP(double heightP){
        this.heightP = heightP;
    }
    public double getHeightP(){
        return heightP;
    }
    public void setWidthP(double widthP){
        this.widthP = widthP;
    }
    public double getWidthP(){
        return widthP;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }
    public Tire getTire() {
        return tire;
    }
    public void setParentVin(String parentVin) {
        this.parentVin = parentVin;
    }

    public String getParentVin() {
        return parentVin;
    }
}
