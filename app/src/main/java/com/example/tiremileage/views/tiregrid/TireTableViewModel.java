package com.example.tiremileage.views.tiregrid;

import android.app.Application;
import androidx.lifecycle.*;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;

import java.util.List;

public class TireTableViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<Tire>> allTires;
    public TireTableViewModel(Application application){
        super(application);
        allTires = LiveDataReactiveStreams.fromPublisher(
                Repository.getAllTired(application.getApplicationContext())
                .onErrorReturn(error -> null));
    }
}
