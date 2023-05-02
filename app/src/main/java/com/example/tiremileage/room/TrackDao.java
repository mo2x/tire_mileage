package com.example.tiremileage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.tiremileage.room.Entities.TrackWithValidTires;
import com.example.tiremileage.room.Entities.Track;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface TrackDao {
    @Insert
    void insertAllTracks(Track... tracks);
    @Query("SELECT * FROM track")
    Flowable<List<Track>> getAllTracks();
    @Transaction
    @Query("SELECT * FROM track")
    Flowable<List<TrackWithValidTires>> getTracksWithValidTires();
}
