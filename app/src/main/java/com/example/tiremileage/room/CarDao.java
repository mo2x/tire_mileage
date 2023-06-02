package com.example.tiremileage.room;

import androidx.room.*;
import com.example.tiremileage.room.Entities.Car;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCars(Car... cars);
    @Update
    void updateAllCars(Car... cars);
    @Query("SELECT * FROM Cars")
    Flowable<List<Car>> getAllCars();
    @Query("SELECT * FROM Cars WHERE vin == :vin")
    Flowable<Car> getCar(String vin);
}
