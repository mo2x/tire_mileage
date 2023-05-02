package com.example.tiremileage.views.tiregrid;

import android.app.Application;
import androidx.lifecycle.*;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.Entities.Tire;

import java.util.List;

public class TireTableViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<Tire>> allTires;

    public TireTableViewModel(Application application){
        super(application);
        repository = new Repository();
        allTires = repository.getAllTires(application.getApplicationContext());
    }
}
