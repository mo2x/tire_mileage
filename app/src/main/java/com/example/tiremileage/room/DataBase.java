package com.example.tiremileage.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.tiremileage.room.Entities.Track;
import com.example.tiremileage.room.Entities.Tire;

@Database(entities = {Tire.class, Track.class}, version = 2)
abstract public class DataBase extends RoomDatabase {
    public abstract TireDao tireDao();
    public abstract TrackDao trackDao();
    static private DataBase INSTANCE = null;
    public static DataBase getDataBase(Context context) {
        if (INSTANCE == null) synchronized (DataBase.class) {
            INSTANCE = Room.databaseBuilder(context, DataBase.class, "DB.db").
                    createFromAsset("db/DBTireTrack.db").
                    fallbackToDestructiveMigration().
                    build();
        }
        return INSTANCE;
    }
}
