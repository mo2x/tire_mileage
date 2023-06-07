package com.example.tiremileage.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.customItems.Status;
import com.example.tiremileage.net.JSONParser;
import com.example.tiremileage.net.OkHTTPModule;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Model;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.views.constructor.ConstructorViewModel;
import okhttp3.Cookie;
import org.json.JSONException;

import java.io.IOException;
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
    public MutableLiveData<String> currentSpinVin = new MutableLiveData<>();

    public Repository(Application application){
        this.application = application;
        sharedPreferences = application.getSharedPreferences("myData", Context.MODE_PRIVATE);
    }
    public void initOkHTTPModule(){
        okHTTPModule = new OkHTTPModule();
    }

    public void authorization(String login, String password, Handler handler){
        okHTTPModule.authorization(scriptUrl, login, password, handler);
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
                    viewModel.addCars(cars);
                    viewModel.postStatus(Status.LOADED);
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
                Model retModel = JSONParser.getConnectors(stringJSON, application.getApplicationContext(),model);
                String string = okHTTPModule.getTiresJSON(scriptUrl, viewModel.getVin(), null);
                Tire[] tires = JSONParser.getTires(string);
                for (int i = 0; i < tires.length; i++){
                    for (int j = 0; j<retModel.connectors.length; j++){
                        if (Integer.parseInt(tires[i].pos) == retModel.connectors[j].getPosition()){
                            retModel.connectors[j].setTire(tires[i]);
                            break;
                        }
                    }
                }
                viewModel.postModelMap(retModel);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
            isRequestedModel = false;
        }).start();
    }
    public void getTires(){
        new Thread(() -> {
            //okHTTPModule.getTires(scriptUrl);
        }).start();
    }
    public synchronized  void saveCookie(Cookie cookie){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookie.value", cookie.value());
        editor.putString("cookie.domain", cookie.domain());
        editor.putString("cookie.name", cookie.name());
        editor.putString("cookie.path", cookie.path());
        editor.apply();
    }
    public List<Cookie> loadCookie(){
        if (sharedPreferences.getString("cookie.domain", "").equals("")){
            return null;
        }
        Cookie cookie = new Cookie.Builder()
                .domain(sharedPreferences.getString("cookie.domain", ""))
                .value(sharedPreferences.getString("cookie.value", ""))
                .name(sharedPreferences.getString("cookie.name", ""))
                .path(sharedPreferences.getString("cookie.path", ""))
                .secure()
                .build();
        List<Cookie> list = new ArrayList<Cookie>();
        list.add(cookie);
        return list;
    }
    public void clearSession(){
        new Thread(() -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("cookie.value");
            editor.remove("cookie.domain");
            editor.remove("cookie.name");
            editor.remove("cookie.path");
            editor.apply();
        });
        if (okHTTPModule == null){
            okHTTPModule = new OkHTTPModule();
        }
        okHTTPModule.clear();
    }
    public void checkAuthAsync(Handler handler){
        new Thread(() -> {
            okHTTPModule.checkAuth(scriptUrl, handler);
        }).start();
    }
}
