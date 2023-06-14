package com.example.tiremileage.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.net.JSONParser;
import com.example.tiremileage.net.OkHTTPModule;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Model;
import com.example.tiremileage.room.Entities.Monitor;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.views.analytic.AnalyticViewModel;
import com.example.tiremileage.views.constructor.ConstructorViewModel;
import com.example.tiremileage.views.tiregrid.TireTableViewModel;
import okhttp3.Cookie;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Repository {
    public Application application;
    SharedPreferences sharedPreferences;
    OkHTTPModule okHTTPModule;
    String scriptUrl = "http://f0822897.xsph.ru";
    boolean isRequestedCar = false;
    boolean isRequestedModel = false;
    boolean isRequestedTires = false;
    public String currentSpinVin;
    public int currentTireId;
    public ConstructorViewModel constructorViewModel;
    public TireTableViewModel tireTableViewModel;

    public Repository(Application application){
        this.application = application;
        sharedPreferences = application.getSharedPreferences("myData", Context.MODE_PRIVATE);
    }
    public void initOkHTTPModule(){
        okHTTPModule = new OkHTTPModule();
    }
    public void getCars(String search, int firstEl, int countEl, ConstructorViewModel viewModel){
        if (isRequestedCar) return;
        new Thread(() -> {
            isRequestedCar = true;
            String stringJSON = okHTTPModule.getCarsJSON(scriptUrl, search, firstEl, countEl);
            try {
                if (stringJSON.equals("timeout")) {
                    viewModel.postStatus(Status.NO_INTERNET);
                } else if (stringJSON.charAt(0)!='[') {
                    viewModel.postStatus(Status.NO_SERVER_RESPONSE);
                } else if (stringJSON.equals("[]")) {
                    viewModel.postStatus(Status.ALL_LOADED);
                } else {
                    List<Car> cars = new ArrayList<>(Arrays.asList(JSONParser.getCars(stringJSON)));
                    viewModel.postStatus(Status.LOADED);
                    viewModel.addCars(cars);
                }
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
            isRequestedCar = false;
        }).start();
    }
    public void getModel(String model, ConstructorViewModel viewModel){
        if (isRequestedModel) return;
        isRequestedModel = true;
        new Thread(() -> {
            String stringJSON = okHTTPModule.getModelJSON(scriptUrl, model);
            try {
                currentSpinVin = viewModel.getVin();
                Model retModel = JSONParser.getConnectors(stringJSON, application.getApplicationContext(),model,viewModel.getVin());
                String string = okHTTPModule.getTiresJSONByVin(scriptUrl, viewModel.getVin());
                Tire[] tires = JSONParser.getTires(string);
                for (int i = 0; i < tires.length; i++){
                    for (int j = 0; j<retModel.connectors.length; j++){
                        if (Integer.parseInt(tires[i].pos) == retModel.connectors[j].getPosition()){
                            retModel.connectors[j].setTire(tires[i]);
                            break;
                        }
                    }
                }
                List<String> sizes = new ArrayList<>();
                sizes.add(retModel.connectors[0].getTireSizeDouble());
                for (int i = 1; i<retModel.connectors.length; i++){
                    if (retModel.connectors[i].getTire()==null) {
                        break;
                    }
                    boolean isIn = false;
                    for (int j = 0; j<sizes.size();j++){
                        if (retModel.connectors[i].getTire().tSize.equals(sizes.get(j))){
                            isIn = true;
                        }
                    }
                    if (!isIn){
                        sizes.add(retModel.connectors[i].getTire().tSize);
                    }
                }
                viewModel.postActualSizes(sizes);
                viewModel.postModelMap(retModel);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
            viewModel.clearTirePool();
            viewModel.postTireViewStatus(Status.IS_LOADING);
            isRequestedModel = false;
        }).start();
    }
    public void getTires(String[] tireSizes, String search, int firstEl, int itemCount, ConstructorViewModel viewModel){
        if (isRequestedTires) return;
        isRequestedTires = true;
        new Thread(() -> {
            String string = okHTTPModule.getTiresJSONNoVin(scriptUrl, tireSizes, search, firstEl, itemCount);
            try {
                Tire[] tires = JSONParser.getTires(string);
                if (tires.length == 0)
                    viewModel.postTireViewStatus(Status.ALL_LOADED);
                else
                    viewModel.postTireViewStatus(Status.LOADED);
                List<Tire> tiresValue = viewModel.getTiresValue();
                List<Tire> addTire = Arrays.asList(tires);
                tiresValue.addAll(addTire);
                viewModel.postTiresValue(tiresValue);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
            isRequestedTires=false;
        }).start();
    }
    public void getTires(TireTableViewModel viewModel){
        tireTableViewModel = viewModel;
        getTires();
    }
    public void getTires(){
        if (isRequestedTires) return;
        isRequestedTires = true;
        new Thread(() -> {
            if (currentSpinVin != null) {
                String string = okHTTPModule.getTiresJSONByVin(scriptUrl, currentSpinVin);
                try {
                    Tire[] tires = JSONParser.getTires(string);
                    tireTableViewModel.postTirePool(Arrays.asList(tires));
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
            isRequestedTires=false;
        }).start();
    }
    public void postTire(String tireID, int pos, String vin){
        new Thread(() -> {
            okHTTPModule.postTireVin(scriptUrl,tireID,pos,vin);
            constructorViewModel.getModel(constructorViewModel.getCurrentModel().modelSn);
        }).start();
    }
    public void getMonitor(AnalyticViewModel viewModel){
        new Thread(() -> {
            String string = okHTTPModule.getMonitorJSON(scriptUrl,currentTireId);
            Monitor[] monitors;
            try {
                monitors = JSONParser.getMonitor(string);
                viewModel.monitor.postValue(Arrays.asList(monitors));
            } catch (JSONException | ParseException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
