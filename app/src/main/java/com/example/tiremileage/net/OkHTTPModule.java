package com.example.tiremileage.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.example.tiremileage.repository.RepositoryManager;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

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
    private final MyCookieJar cookieJar = new MyCookieJar();
    private final OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

    public String getModel(String url, int model){
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
    public void getTires(String url){
        Request request = new Request.Builder().url(url+"/get_tires.php").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Request request = new Request.Builder().url(url+"/get_tires.php").build();
                JSONParser jsonParser = new JSONParser();
                try {
                    RepositoryManager.getRepository().insert(jsonParser.getTires(client.newCall(request).execute().body().string()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void checkAuth(String url, Handler handler){
        Request request = new Request.Builder().url(url+"/check_session.php").build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            assert response.body() != null;
            if (response.body().string().equals("0")) {
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
        }
    }
    public void authorization(String url, String login, String password, Handler handler) {
        RequestBody requestBody = new FormBody.Builder()
                .add("login", login)
                .add("password", password)
                .build();
        Request request = new Request.Builder().url(url+"/login.php").post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    handler.sendEmptyMessage(0);
                } else if (response.code() == 409){
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(-1);
            }
        });
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

    public void clear(){
        cookieJar.clear();
    }
    private static class MyCookieJar implements CookieJar {
        private List<Cookie> cookies;
        public MyCookieJar(){
            cookies = RepositoryManager.getRepository().loadCookie();
        }

        public void clear(){
            cookies = null;
        }
        @Override
        public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
            this.cookies =  cookies;
            new Thread(() -> {
                RepositoryManager.getRepository().saveCookie(cookies.get(0));
            }).start();
        }
        @Override
        public @NotNull List<Cookie> loadForRequest(@NotNull HttpUrl url) {
            if (cookies != null)
                return cookies;
            return new ArrayList<>();
        }
    }
}
