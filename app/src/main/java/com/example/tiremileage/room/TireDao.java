package com.example.tiremileage.room;

import androidx.room.*;
import com.example.tiremileage.room.Entities.Tire;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface TireDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTires(Tire... tires);
    @Query("SELECT * FROM tires")
    Flowable<List<Tire>> getAllTires();
    @Query("SELECT * FROM tires WHERE id = :id ")
    Tire getTireByID(int id);
    @Update
    void update(Tire tire);
}
