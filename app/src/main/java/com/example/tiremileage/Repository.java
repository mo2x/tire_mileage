package com.example.tiremileage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import com.example.tiremileage.room.TireDao;
import io.reactivex.Flowable;

import java.util.List;

public class Repository {

    TireDao tireDao;
    public static class TireShadowBuilder extends View.DragShadowBuilder {


        Drawable shadow;
        ImageView imageView;
        public TireShadowBuilder(ImageView imageView){
            super(imageView);
            this.imageView = imageView;
            shadow = imageView.getDrawable();
        }


        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint);
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            shadow.setBounds(0,0,width,height);
            outShadowSize.set(width,height);
            outShadowTouchPoint.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
            shadow.draw(canvas);
        }
    }
    public LiveData<List<Tire>> getAllTired(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                        .getDataBase(context.getApplicationContext())
                        .tireDao()
                        .getAllTires());
    }
    public LiveData<List<Track>> getAllTracks(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                        .getDataBase(context.getApplicationContext())
                        .trackDao()
                        .getAllTracks());
    }

    public Tire getTireByID(Context context, int id){
        return DataBase
                        .getDataBase(context.getApplicationContext())
                        .tireDao()
                        .getTireByID(id);
    }

    public Track getTrackByVin(Context context, String VIN){
        return DataBase.getDataBase(context.getApplicationContext()).trackDao().getTrackByVin(VIN);
    }

    public static Track currentTrack;
    @WorkerThread
    public void update(Context context, Tire tire){
        DataBase.getDataBase(context.getApplicationContext()).tireDao().update(tire);
    }
}
