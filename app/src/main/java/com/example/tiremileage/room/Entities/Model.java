package com.example.tiremileage.room.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.tiremileage.customItems.Connector;

@Entity(tableName = "models")
public class Model {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    public String modelSn;
    public Connector[] connectors;
}
