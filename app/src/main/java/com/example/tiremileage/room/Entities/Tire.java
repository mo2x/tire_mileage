package com.example.tiremileage.room.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
public class Tire {
    public int id;
    public String serialNumber;
    public double km;
    public String tSize;
    public double treadDepth;
    public double tkph;
    public double kpa;
    public double p_kph;
    public String pic;
    public String maker_name;
    public String vin;
    public String pos;
    public int tepm;
}


