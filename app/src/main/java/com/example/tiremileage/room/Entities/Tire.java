package com.example.tiremileage.room.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "tire")
public class Tire {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "serial_num")
    public String serialNumber;
    @ColumnInfo(name = "km")
    public float km;
    @ColumnInfo(name = "t_size")
    public String tSize;
    @ColumnInfo(name = "tread_depth")
    public float treadDepth;
    @ColumnInfo(name = "tkph")
    public float tkph;
    @ColumnInfo(name = "kpa")
    public String kpa;
    @ColumnInfo(name = "p_kph")
    public float p_kph;
    @ColumnInfo(name = "pic")
    public String pic;
    @ColumnInfo(name = "maker_name")
    public String maker_name;
    @ColumnInfo(name = "parent_vin")
    public String vin;
    @ColumnInfo(name = "pos")
    public String pos;
}


