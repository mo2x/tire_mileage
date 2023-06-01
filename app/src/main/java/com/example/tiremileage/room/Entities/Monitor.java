package com.example.tiremileage.room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "monitor")
public class Monitor {
    @PrimaryKey
    public int id;
}
