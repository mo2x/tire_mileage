package com.example.tiremileage;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import com.example.tiremileage.room.TireDao;
import io.reactivex.Flowable;

import java.util.List;

public class Repository {



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

    public Track getTrackByVin(Context context, String VIN){
        return DataBase.getDataBase(context.getApplicationContext()).trackDao().getTrackByVin(VIN);
    }
}
