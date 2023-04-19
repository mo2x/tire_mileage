package com.example.tiremileage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.tiremileage.room.Entities.Track;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface TrackDao {
    @Insert
    void insertAllTires(Track... tracks);
    @Query("SELECT * FROM track")
    Flowable<List<Track>> getAllTracks();
}
