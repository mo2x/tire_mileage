package com.example.tiremileage.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
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
    Flowable<Tire> getTireByID(int id);
}
