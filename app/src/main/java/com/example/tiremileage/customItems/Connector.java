package com.example.tiremileage.customItems;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.example.tiremileage.R;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.Entities.Tire;

public class Connector extends androidx.appcompat.widget.AppCompatImageView {
    boolean isIn = false;
    int position;
    int tireID = -1;
    ImageView imageView;
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

    public void returnTire(){
        setImageDrawable(null);
        ReturnTireThread thread = new ReturnTireThread(new Repository());
        thread.start();
    }

    private void init(Context context, AttributeSet attrs, int n) {


        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Connector, n, 0);
        position = attributes.getInt(R.styleable.Connector_position, n);
        attributes.recycle();
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (tireID == -1){
                    return false;
                }
                ClipData.Item item = new ClipData.Item(String.valueOf(tireID));
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData("CTire",mimeTypes, item);
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
                View dragView =(View) event.getLocalState();
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
                        if (isIn){

                            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                                if (event.getClipDescription().getLabel().toString().equals("tire")) {
                                    TireItem item = (TireItem) dragView;
                                    setImageDrawable(item.imageView.getDrawable());
                                    imageView = item.imageView;

                                }
                                if (event.getClipDescription().getLabel().toString().equals("CTire")){
                                    Connector item = (Connector) dragView;
                                    setImageDrawable(item.imageView.getDrawable());
                                    imageView = item.imageView;
                                    item.imageView = null;
                                    item.setVisibility(VISIBLE);
                                    item.setImageDrawable(null);
                                }
                                CharSequence draggedData = event.getClipData().getItemAt(0).getText();
                                tireID = Integer.parseInt(draggedData.toString());
                                Repository repository = new Repository();
                                MyThread myThread = new MyThread(repository, tireID);
                                myThread.start();
                            }
                        }
                        break;
                    default: break;
                }
                return true;


            }
        });
    }
    public class MyThread extends Thread {
        Repository repository;
        int data;
        MyThread(Repository repository, int data){
            this.repository = repository;
            this.data = data;
        }
        public void run() {
            Tire tire = repository.getTireByID(getContext(), data);
            tire.vin = Repository.currentTrack.vin;
            tire.pos = String.valueOf(position);
            repository.update(getContext(),tire);
        }
    }
    public class ReturnTireThread extends Thread{
        Repository repository;
        ReturnTireThread(Repository repository){
            this.repository = repository;
        }
        public void run(){
            Tire tire = repository.getTireByID(getContext(), tireID);
            tire.vin = "-";
            tire.pos = "";
            repository.update(getContext(),tire);
            tireID = -1;
        }
    }
}
