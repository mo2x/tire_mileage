package com.example.tiremileage;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import com.example.tiremileage.room.DataBase;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import com.example.tiremileage.room.Entities.TrackWithValidTires;

import java.util.List;

public class Repository {


    public LiveData<List<TrackWithValidTires>> getTracksWithValidTires(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                .getDataBase(context.getApplicationContext())
                .trackDao()
                .getTracksWithValidTires());
    }

    public LiveData<List<Tire>> getAllTires(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                        .getDataBase(context.getApplicationContext())
                        .tireDao()
                        .getAllTires());
    }
    public LiveData<List<Track>> getAllTracks(Context context){
        return LiveDataReactiveStreams
                .fromPublisher(DataBase
                        .getDataBase(context.getApplicationContext())
                        .trackDao()
                        .getAllTracks());
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
    public void insert(Context context, Track[] tracks){
        InsertThread insertThread = new InsertThread(context, tracks);
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
        Track[] tracks;
        private InsertThread(Context context, Tire[] tires){
            this.tracks = new Track[0];
            this.tires = tires;
            this.context = context;
        }

        private InsertThread(Context context, Track[] tracks){
            this.tracks = tracks;
            this.tires = new Tire[0];
            this.context = context;
        }
        @Override
        public void run() {
            DataBase.getDataBase(context.getApplicationContext()).tireDao().insertAllTires(tires);
            DataBase.getDataBase(context.getApplicationContext()).trackDao().insertAllTracks(tracks);
        }
    }
}
