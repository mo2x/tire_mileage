package com.example.tiremileage.views.constructor;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConstructorViewModel extends AndroidViewModel {
    MutableLiveData<Status> status = new MutableLiveData<>();
    MutableLiveData<List<Car>> carPool = new MutableLiveData<>();
    MutableLiveData<String> currentVin = new MutableLiveData<>();
    LiveData<String> model;
    public String searchString;
    public ConstructorViewModel(Application application){
        super(application);
        carPool.postValue(new ArrayList<>());
        searchString = "";
        status.postValue(Status.IS_LOADING);
        currentVin.postValue("");
    }
    public void loadPoolObj(String s){
        if (status.getValue() != Status.ALL_LOADED) {
            status.postValue(Status.IS_LOADING);
            RepositoryManager.getRepository().getCars(s, carPool.getValue().size(), 10, this);
        }
    }
    public void addCars(List<Car> list){
        List<Car> cars= new ArrayList<>(Objects.requireNonNull(carPool.getValue()));
        cars.addAll(list);
        carPool.postValue(cars);
    }
    public void setStatus(Status status){
        this.status.postValue(status);
    }
    public void clearCars(){
        carPool.postValue(new ArrayList<>());
    }
}
