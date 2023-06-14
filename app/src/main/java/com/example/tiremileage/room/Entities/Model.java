package com.example.tiremileage.room.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.tiremileage.customItems.Connector;

public class Model {
    public int id;
    public String modelSn;
    public Connector[] connectors;
}
