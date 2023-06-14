package com.example.tiremileage.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.example.tiremileage.MainActivity;
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
        RepositoryManager.init(getApplication());
        startActivity(mainActivity); finish();
    }
}
