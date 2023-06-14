package com.example.tiremileage.views.constructor;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Model;
import com.example.tiremileage.room.Entities.Tire;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConstructorViewModel extends AndroidViewModel {
    MutableLiveData<Status> tireViewStatus = new MutableLiveData<>();
    MutableLiveData<Status> recyclerViewStatus = new MutableLiveData<>();
    MutableLiveData<Status> fragmentStatus = new MutableLiveData<>();
    MutableLiveData<List<Car>> carPool = new MutableLiveData<>();
    MutableLiveData<List<Tire>> tirePool = new MutableLiveData<>();
    MutableLiveData<String> currentVin = new MutableLiveData<>();
    MutableLiveData<Model> model = new MutableLiveData<>();
    MutableLiveData<List<String>> actualSizes = new MutableLiveData<>();
    public String searchString;
    public ConstructorViewModel(Application application){
        super(application);
        carPool.postValue(new ArrayList<>());
        searchString = "";
        recyclerViewStatus.postValue(Status.IS_LOADING);
        fragmentStatus.postValue(Status.LOADED);
        tireViewStatus.postValue(Status.ALL_LOADED);
        currentVin.postValue("");
        RepositoryManager.getRepository().currentSpinVin = currentVin.getValue();
        RepositoryManager.getRepository().constructorViewModel = this;
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
    public void clearTirePool(){
        tirePool.postValue(new ArrayList<>());
    }
    public String getVin(){
        return currentVin.getValue();
    }
    public void postActualSizes(List<String> actualSizes) {
        this.actualSizes.postValue(actualSizes);
    }
    public void getTires(String search){
        RepositoryManager.getRepository()
                .getTires(actualSizes.getValue().toArray(new String[0]),
                        search,
                        tirePool.getValue().size(),
                        10,
                        this);
    }
    public List<Tire> getTiresValue(){
        return tirePool.getValue();
    }
    public void postTiresValue(List<Tire> tires){
        tirePool.postValue(tires);
    }
    public void postTireViewStatus(Status status){
        tireViewStatus.postValue(status);
    }
    public void removeTire(String tireID){

    }
    public Model getCurrentModel(){
        return model.getValue();
    }
}
