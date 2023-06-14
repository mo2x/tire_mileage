package com.example.tiremileage.views.analytic;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiremileage.repository.RepositoryManager;
import com.example.tiremileage.room.Entities.Model;
import com.example.tiremileage.room.Entities.Monitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnalyticViewModel extends AndroidViewModel {
    public MutableLiveData<List<Monitor>> monitor = new MutableLiveData<>();
    public AnalyticViewModel(@NotNull Application application) {
        super(application);
    }
    public void updateMonitor(){
        RepositoryManager.getRepository().getMonitor(this);
    }
}
