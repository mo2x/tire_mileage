package com.example.tiremileage.net;

import android.content.Context;
import com.example.tiremileage.customItems.Connector;
import com.example.tiremileage.room.Entities.Car;
import com.example.tiremileage.room.Entities.Model;
import com.example.tiremileage.room.Entities.Tire;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {

    public static Tire[] getTires(String string) throws JSONException, IOException {
        JSONArray items = new JSONArray(string);
        Tire[] tires = new Tire[items.length()];
        for (int i = 0; i < items.length(); i++) {
            JSONObject tire = items.getJSONObject(i);
            Tire newTire = new Tire();
            tires[i] = newTire;
            tires[i].id = tire.getInt("id");
            tires[i].serialNumber = tire.getString("sn");
            tires[i].pos = tire.getString("pos");
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

    public static Car[] getCars(String string) throws JSONException, IOException{
        JSONArray items = new JSONArray(string);
        Car[] cars = new Car[items.length()];
        for (int i = 0; i < items.length(); i++) {
            JSONObject track = items.getJSONObject(i);
            Car newCar = new Car();
            cars[i] = newCar;
            cars[i].id = track.getInt("id");
            cars[i].model = track.getString("model");
            cars[i].vin = track.getString("vin");
        }
        return cars;
    }

    public static Model getConnectors(String string,Context context,String modelSn) throws JSONException {
        JSONArray items = new JSONArray(string);
        JSONObject track = items.getJSONObject(0);
        JSONObject tireMap = new JSONObject(track.getString("tire_map"));
        JSONArray apos = tireMap.getJSONArray("apos");
        JSONObject frame = tireMap.getJSONObject("frame");
        JSONObject wheel = tireMap.getJSONObject("wheel");
        int width = Integer.parseInt(frame.getString("width").replace("px",""));
        int height = Integer.parseInt(frame.getString("height").replace("px",""));
        //JSONArray array = new JSONArray(apos.);
        Connector[] connectors = new Connector[apos.length()];
        for (int i = 0; i< apos.length(); i++) {
            connectors[i] = new Connector(context);
            JSONObject connectorObj = apos.getJSONObject(i);

            int tirePosition = Integer.parseInt(connectorObj.getString("id").substring(4));
            connectors[i].setPosition(tirePosition);

            int left = Integer.parseInt(connectorObj.getString("left").replace("px",""));
            int top = Integer.parseInt(connectorObj.getString("top").replace("px",""));

            int x = wheel.getInt("x");
            int y = wheel.getInt("y");

            connectors[i].setLeftDouble(( (left+((double)x/2)))/width);
            connectors[i].setTopDouble(((top+((double)y/2)))/height);

            connectors[i].setWidthP(((double)x)/width);
            connectors[i].setHeightP(((double)y)/height);
            connectors[i].setTireSize(connectorObj.getString("t-size"));

        }
        Model model = new Model();
        model.modelSn = modelSn;
        model.connectors = connectors;
        return model;
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
