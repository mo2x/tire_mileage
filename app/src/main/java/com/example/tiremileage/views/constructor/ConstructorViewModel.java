package com.example.tiremileage.views.constructor;

import android.app.Application;
import android.view.View;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConstructorViewModel extends AndroidViewModel {
    MutableLiveData<Status> recyclerViewStatus = new MutableLiveData<>();
    MutableLiveData<Status> fragmentStatus = new MutableLiveData<>();
    MutableLiveData<List<Car>> carPool = new MutableLiveData<>();
    MutableLiveData<String> currentVin = new MutableLiveData<>();
    MutableLiveData<Model> model = new MutableLiveData<>();
    public String searchString;
    public ConstructorViewModel(Application application){
        super(application);
        carPool.postValue(new ArrayList<>());
        searchString = "";
        recyclerViewStatus.postValue(Status.IS_LOADING);
        fragmentStatus.postValue(Status.LOADED);
        currentVin.postValue("");
    }
    public void loadPoolObj(String s){
        if (recyclerViewStatus.getValue() != Status.ALL_LOADED) {
            recyclerViewStatus.postValue(Status.IS_LOADING);
            RepositoryManager.getRepository().getCars(s, carPool.getValue().size(), 10, this);
        }
    }
    public void addCars(List<Car> list){
        List<Car> cars= new ArrayList<>(Objects.requireNonNull(carPool.getValue()));
        cars.addAll(list);
        carPool.postValue(cars);
    }
    public void postStatus(Status status){
        this.recyclerViewStatus.postValue(status);
    }
    public void setRecyclerViewStatus(Status recyclerViewStatus){
        this.recyclerViewStatus.setValue(recyclerViewStatus);
    }
    public void clearCars(){
        carPool.setValue(new ArrayList<>());
    }
    public void postModelMap(Model inputMap){
        model.postValue(inputMap);
    }
    public void getModel(String model){
        RepositoryManager.getRepository().getModel(model, this);
    }
    public void clearModel(){
        model.postValue(null);
    }
    public String getVin(){
        return currentVin.getValue();
    }
}
