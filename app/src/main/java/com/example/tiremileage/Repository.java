package com.example.tiremileage;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import com.example.tiremileage.room.TireDao;
import io.reactivex.Flowable;

import java.util.List;

public class Repository {


    public static Flowable<List<Tire>> getAllTired(Context context){
        return DataBase.getDataBase(context.getApplicationContext()).tireDao().getAllTires();
    }
    public static Flowable<List<Track>> getAllTracks(Context context){
        return DataBase.getDataBase(context.getApplicationContext()).trackDao().getAllTracks();
    }

    public static Track getTrackByVin(Context context, String VIN){
        return DataBase.getDataBase(context.getApplicationContext()).trackDao().getTrackByVin(VIN);
    }
    private Repository(){}
}
