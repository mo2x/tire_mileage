package com.example.tiremileage.room.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "cars")
public class Car {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "vin")
    public String vin;
    @ColumnInfo(name = "model")
    public String model;
}
