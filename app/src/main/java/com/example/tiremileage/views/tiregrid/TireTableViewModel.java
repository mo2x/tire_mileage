package com.example.tiremileage.views.tiregrid;

import android.app.Application;
import androidx.lifecycle.*;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Tire;

import java.util.List;

public class TireTableViewModel extends AndroidViewModel {

    MutableLiveData<List<Tire>> tirePool = new MutableLiveData<>();
    public TireTableViewModel(Application application){
        super(application);
        RepositoryManager.getRepository().tireTableViewModel = this;
    }
    public void updateTirePool(){
        RepositoryManager.getRepository().getTires(this);
    }
    public void postTirePool(List<Tire> tires){
        tirePool.postValue(tires);
    }
    public void setCurrentTire(Tire tire){
        RepositoryManager.getRepository().currentTireId = tire.id;
    }
}
