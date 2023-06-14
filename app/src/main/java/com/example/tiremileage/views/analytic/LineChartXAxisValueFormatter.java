package com.example.tiremileage.views.analytic;

import android.icu.text.DateFormat;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.Date;
import java.util.Locale;

public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {

    @Override
    public String getFormattedValue(float value) {

        long emissionsMilliSince1970Time = ((long) value) * 1000;

        Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
        String date_day = String.valueOf(timeMilliseconds.getDate());
        String date_month = String.valueOf(timeMilliseconds.getMonth());
        String date_year = String.valueOf(timeMilliseconds.getYear());
        String date_hours = String.valueOf(timeMilliseconds.getHours());

        return date_day+"/"+
                date_month+"/"+
                date_year+"\n"+
                date_hours+"h";
    }
}