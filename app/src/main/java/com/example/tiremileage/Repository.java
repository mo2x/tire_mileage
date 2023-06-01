package com.example.tiremileage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.net.OkHTTPModule;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Tire;
import okhttp3.Cookie;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    static String loginScriptUrl = "http://f0822897.xsph.ru";
    static SharedPreferences sharedPreferences;
    static OkHTTPModule okHTTPModule;
    static public MutableLiveData<String> currentSpinVin = new MutableLiveData<>();

    public void authorization(String login, String password, Handler handler){
        okHTTPModule.authorization(loginScriptUrl, login, password, handler);
    }
    public void getCars(){
        new Thread(() -> {
            okHTTPModule.getCars(loginScriptUrl);
        }).start();
    }
    public static synchronized  void saveCookie(Cookie cookie){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookie.value", cookie.value());
        editor.putString("cookie.domain", cookie.domain());
        editor.putString("cookie.name", cookie.name());
        editor.putString("cookie.path", cookie.path());
        editor.apply();
    }
    public static List<Cookie> loadCookie(){
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
    public static void clearSession(){
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
    public static void checkAuth(Handler handler){
        if (okHTTPModule == null){
            okHTTPModule = new OkHTTPModule();
        }
        new Thread(() -> {
            okHTTPModule.checkAuth(loginScriptUrl, handler);
        }).start();
    }
    public LiveData<List<Tire>> getAllTires(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                        .getDataBase(context.getApplicationContext())
                        .tireDao()
                        .getAllTires());
    }
    public LiveData<List<Car>> getAllTracks(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                        .getDataBase(context.getApplicationContext())
                        .carDao()
                        .getAllCars());
    }
    public void update(Context context, Tire tire){
        UpdateThread updateThread = new UpdateThread(context, tire);
        updateThread.start();
    }

    public void updateTirePosByID(Context context, int tireID, String newPos){
        UpdateTirePosByIDThread updateThread = new UpdateTirePosByIDThread(context, tireID, newPos);
        updateThread.start();
    }

    public void insert(Context context, Tire[] tires){
        InsertThread insertThread = new InsertThread(context, tires);
        insertThread.start();
    }
    public void insert(Context context, Car[] cars){
        InsertThread insertThread = new InsertThread(context, cars);
        insertThread.start();
    }
    private static class UpdateTirePosByIDThread extends Thread{
        Context context;
        int tireID;
        String pos;
        UpdateTirePosByIDThread(Context context, int tireID, String pos){
            this.context = context;
            this.tireID = tireID;
            this.pos = pos;
        }
        @Override
        public void run() {
            Tire tire = DataBase
                    .getDataBase(context.getApplicationContext())
                    .tireDao()
                    .getTireByID(tireID);

            tire.pos = pos;

            DataBase
                    .getDataBase(context.getApplicationContext())
                    .tireDao()
                    .update(tire);
        }
    }
    private static class UpdateThread extends Thread{
        Context context;
        Tire tire;
        private UpdateThread(Context context, Tire tire){
            this.tire = tire;
            this.context = context;
        }
        @Override
        public void run() {
            DataBase.getDataBase(context.getApplicationContext()).tireDao().update(tire);
        }
    }
    private static class InsertThread extends Thread{
        Context context;
        Tire[] tires;
        Car[] cars;
        private InsertThread(Context context, Tire[] tires){
            this.cars = new Car[0];
            this.tires = tires;
            this.context = context;
        }
        private InsertThread(Context context, Car[] cars){
            this.cars = cars;
            this.tires = new Tire[0];
            this.context = context;
        }
        @Override
        public void run() {
            DataBase.getDataBase(context.getApplicationContext()).tireDao().insertAllTires(tires);
            DataBase.getDataBase(context.getApplicationContext()).carDao().insertAllCars(cars);
        }
    }
}
