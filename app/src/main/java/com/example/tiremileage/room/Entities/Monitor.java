package com.example.tiremileage.room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

public class Monitor {
    public int id;
    public Date date;
    public int tireId;
    public double temperature;
    public double kpa;
    public double tread_depth;
}
