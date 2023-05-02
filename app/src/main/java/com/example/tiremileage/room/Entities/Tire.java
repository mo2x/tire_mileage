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
    public double km;
    @ColumnInfo(name = "t_size")
    public String tSize;
    @ColumnInfo(name = "tread_depth")
    public double treadDepth;
    @ColumnInfo(name = "tkph")
    public double tkph;
    @ColumnInfo(name = "kpa")
    public double kpa;
    @ColumnInfo(name = "p_kph")
    public double p_kph;
    @ColumnInfo(name = "pic")
    public String pic;
    @ColumnInfo(name = "maker_name")
    public String maker_name;
    @ColumnInfo(name = "vin")
    public String vin;
    @ColumnInfo(name = "pos")
    public String pos;
}


