package com.example.tiremileage.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.tiremileage.LoginActivity;
import com.example.tiremileage.MainActivity;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.repository.RepositoryManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    final int HAS_NO_SESSION = 0;
    final int HAS_SESSION = 1;
    final int HAS_NO_INTERNET = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainActivity = new Intent(this, MainActivity.class);
        Intent loginActivity = new Intent(this, LoginActivity.class);
        RepositoryManager.init(getApplication());
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case(HAS_SESSION): startActivity(mainActivity); finish();break;
                    case(HAS_NO_SESSION): startActivity(loginActivity); finish();break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + msg.what);
                }
            }
        };
        RepositoryManager.getRepository().checkAuthAsync(handler);
    }
}
