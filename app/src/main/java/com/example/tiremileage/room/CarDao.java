package com.example.tiremileage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.tiremileage.room.Entities.Car;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface CarDao {
    @Insert
    void insertAllCars(Car... cars);
    @Query("SELECT * FROM Cars")
    Flowable<List<Car>> getAllCars();
    @Query("SELECT * FROM Cars WHERE vin == :vin")
    Flowable<Car> getCar(String vin);
}
