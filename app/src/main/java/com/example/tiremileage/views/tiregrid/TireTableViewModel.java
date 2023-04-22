package com.example.tiremileage.views.tiregrid;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.*;
import com.example.tiremileage.R;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;

import java.util.ArrayList;
import java.util.List;

public class TireTableViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<Tire>> allTires;

    public TireTableViewModel(Application application){
        super(application);
        repository = new Repository();
        allTires = repository.getAllTired(application.getApplicationContext());
    }
}
