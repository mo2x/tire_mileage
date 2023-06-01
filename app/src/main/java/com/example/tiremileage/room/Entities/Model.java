package com.example.tiremileage.room.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "models")
public class Model {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "model_sn")
    public String modelSn;
    @ColumnInfo(name = "tire_map")
    public String tire_map;
}
