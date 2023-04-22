package com.example.tiremileage.viewa.constructor;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.SortedList;
import com.example.tiremileage.Repository;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;

import java.util.List;

public class ConstructorViewModel  extends AndroidViewModel {
    private MutableLiveData<String> currentFragRes;

    public LiveData<String> getCurrentFragRes() {
        return currentFragRes;
    }

    Repository repository;
    LiveData<List<Tire>> allTires;
    LiveData<List<Track>> allTracks;
    public ConstructorViewModel(Application application){
        super(application);
        repository = new Repository();
        allTires = repository.getAllTired(application.getApplicationContext());
        allTracks = repository.getAllTracks(application.getApplicationContext());
    }

}
