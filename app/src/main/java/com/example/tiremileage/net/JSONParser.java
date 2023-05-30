package com.example.tiremileage.net;

import android.content.Context;
import com.example.tiremileage.R;
import com.example.tiremileage.room.Entities.Tire;
import com.example.tiremileage.room.Entities.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {

    public Tire[] getTires(Context context) throws JSONException, IOException {
        InputStream inputStream = context.getResources().openRawResource(R.raw.tire_log);
        JSONObject object = new JSONObject(convertStreamToString(inputStream));
        JSONArray items = object.getJSONArray("items");
        Tire[] tires = new Tire[items.length()];
        for (int i = 0; i < items.length(); i++) {
            JSONObject tire = items.getJSONObject(i);
            Tire newTire = new Tire();
            tires[i] = newTire;
            tires[i].id = tire.getInt("id");
            tires[i].serialNumber = tire.getString("sn");
            tires[i].pos = "0";
            tires[i].km = tire.getDouble("km");
            tires[i].tSize = tire.getString("tsize");
            tires[i].treadDepth = tire.getDouble("tread_depth");
            tires[i].tkph = tire.getDouble("tkph");
            tires[i].kpa = tire.getDouble("kpa");
            tires[i].p_kph = tire.getDouble("p_kpa");
            tires[i].pic = tire.getString("pic");
            tires[i].maker_name = tire.getString("maker_name");
            tires[i].vin = tire.getString("vin");
        }
        return tires;
    }

    public Track[] getTracks(Context context) throws JSONException, IOException{
        InputStream inputStream = context.getResources().openRawResource(R.raw.car);
        JSONObject object = new JSONObject(convertStreamToString(inputStream));
        JSONArray items = object.getJSONArray("items");
        Track[] tracks = new Track[items.length()];
        for (int i = 0; i < items.length(); i++) {
            JSONObject track = items.getJSONObject(i);
            Track newTrack = new Track();
            tracks[i] = newTrack;
            tracks[i].id = track.getInt("id");
            tracks[i].model = track.getString("model");
            tracks[i].vin = track.getString("vin");
        }
        return tracks;
    }

    private String convertStreamToString(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        stream.close();

        return sb.toString();

    }
}
