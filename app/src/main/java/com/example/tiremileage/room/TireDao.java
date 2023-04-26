package com.example.tiremileage.room;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.tiremileage.room.Entities.Tire;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface TireDao {
    @Insert
    void insertAllTires(Tire... tires);
    @Delete
    void delete(Tire tire);
    @Query("SELECT * FROM tire")
    Flowable<List<Tire>> getAllTires();
    @Query("SELECT * FROM tire WHERE id = :id ")
    Tire getTireByID(int id);
    @Update
    void update(Tire tire);
}
