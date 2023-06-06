package com.example.tiremileage.views.tiregrid;

import android.app.Application;
import androidx.lifecycle.*;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Tire;

import java.util.List;

public class TireTableViewModel extends AndroidViewModel {
    LiveData<List<Tire>> allTires;

    public TireTableViewModel(Application application){
        super(application);
        allTires = RepositoryManager.getRepository().getAllTires(application.getApplicationContext());
    }
}
