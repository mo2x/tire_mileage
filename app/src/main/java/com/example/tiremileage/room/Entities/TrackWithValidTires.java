package com.example.tiremileage.room.Entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TrackWithValidTires {

    @Embedded
    public Track track;

    @Relation(parentColumn = "vin", entityColumn = "vin")
    public List<Tire> tires;
}
