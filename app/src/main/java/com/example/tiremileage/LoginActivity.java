package com.example.tiremileage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.tiremileage.databinding.LogInBinding;
import com.example.tiremileage.repository.Repository;
import com.example.tiremileage.repository.RepositoryManager;


public class LoginActivity extends Activity{
    Handler h;
    LogInBinding logInBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInBinding = LogInBinding.inflate(getLayoutInflater());
        View view = logInBinding.getRoot();
        setContentView(view);
        h = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case -1: Toast.makeText(getApplicationContext(),
                            "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        break;
                    case 0: Toast.makeText(getApplicationContext(),
                            "Приветствую", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1: Toast.makeText(getApplicationContext(),
                            "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: Toast.makeText(getApplicationContext(),
                            "Произошла ошибка на стороне сервера", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        logInBinding.authentication.setOnClickListener(v ->
                RepositoryManager.getRepository().authorization(
                        logInBinding.editTextTextPersonName.getText().toString(),
                        logInBinding.editTextTextPassword.getText().toString(),
                        h));
    }
}
