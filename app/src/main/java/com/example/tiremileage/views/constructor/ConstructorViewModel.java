package com.example.tiremileage.views.constructor;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Car;

import java.util.List;

public class ConstructorViewModel extends AndroidViewModel {
    MutableLiveData<Integer> currentSpinPos = new MutableLiveData<>();
    Repository repository;
    LiveData<List<Tire>> allTires;
    LiveData<List<Car>> allTracks;
    LiveData<String> model;
    public ConstructorViewModel(Application application){
        super(application);
        repository = new Repository();
        allTires = repository.getAllTires(application.getApplicationContext());
        allTracks = repository.getAllTracks(application.getApplicationContext());
    }
    public void postCurrentVin(String vin){
        Repository.currentSpinVin.postValue(vin);
    }
}
