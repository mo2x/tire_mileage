package com.example.tiremileage.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.example.tiremileage.repository.RepositoryManager;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

/*.cookieJar(new CookieJar() {
        private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
            cookieStore.put(url, cookies);
            cookies.get(0).toString();
        }

        @Override
        public @NotNull List<Cookie> loadForRequest(@NotNull HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    })*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OkHTTPModule {
    private final OkHttpClient client = new OkHttpClient.Builder().build();

    public String getModelJSON(String url, String model){
        String requestURL = url + "/get_models.php?"
                +"number=" + model;
        Request request = new Request.Builder().url(requestURL).build();
        try {
            String response = client.newCall(request).execute().body().string();
            return response;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public String getCarsJSON(String url,String search, int firstEl, int countEl){
        String requestURL = url + "/get_cars.php?"
                +"search=" + search
                +"&first_el=" + firstEl
                +"&count_el=" + countEl;
        Request request = new Request.Builder().url(requestURL).build();
        try {
            String response = client.newCall(request).execute().body().string();
            return response;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public String getMonitorJSON(String url,int tireID){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("tire", String.valueOf(tireID));
        FormBody formBody = builder.build();
        String requestURL = url + "/get_monitor.php";
        Request request = new Request.Builder().url(requestURL).post(formBody).build();
        try {
            String response = client.newCall(request).execute().body().string();
            return response;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public String getTiresJSONByVin(String url, String vin){
        FormBody.Builder builder = new FormBody.Builder();
        if (vin != null){
            builder.add("VIN",vin);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().post(formBody).url(url+"/get_tires.php").build();
        try {
            String response = client.newCall(request).execute().body().string();
            return response;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public String getTiresJSONNoVin(String url, String[] tireSizes, String search, int firstEl, int itemCount){
        FormBody.Builder builder = new FormBody.Builder();
        for (int i =0;i<tireSizes.length;i++){
            builder.add("tireSizes"+i,tireSizes[i]);
        }
        builder.add("search", search);
        builder.add("firstEl", String.valueOf(firstEl));
        builder.add("itemCount", String.valueOf(itemCount));

        FormBody formBody = builder.build();
        Request request = new Request.Builder().post(formBody).url(url+"/get_tire_by_vin.php").build();
        try {
            String response = client.newCall(request).execute().body().string();
            return response;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    public void postTireVin(String url, String tireID, int pos, String vin){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("tire_id", tireID);
        builder.add("tire_pos", String.valueOf(pos));
        builder.add("tire_vin", vin);
        FormBody formBody = builder.build();
        Request request = new Request.Builder().post(formBody).url(url+"/post_tire.php").build();

        try {
            String responce = client.newCall(request).execute().body().string();
            System.out.println(responce);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && ((NetworkInfo) wifiInfo).isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
