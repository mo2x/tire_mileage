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
    MutableLiveData<String> currentFragRes;
    LiveData<List<Tire>> allTires;
    LiveData<List<Track>> allTracks;
    public ConstructorViewModel(Application application){
        super(application);
        allTires = LiveDataReactiveStreams.fromPublisher(
                Repository.getAllTired(application.getApplicationContext())
                        .onErrorReturn(error -> null));
        allTracks = LiveDataReactiveStreams.fromPublisher(
                Repository.getAllTracks(application.getApplicationContext())
                        .onErrorReturn(error -> null));
    }

}
