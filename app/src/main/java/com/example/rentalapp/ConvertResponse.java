package com.example.rentalapp;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConvertResponse {
    //helper method for converting api string response to JSONArray, could not get built in
    // networking method to stop throwing errors.
    public static JSONArray stringToJson(String str) {
        try {
            return new JSONArray(str);
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }
        return null;
    }

    public static ArrayList<String> getPropAddressList(String response){
        JSONArray props = ConvertResponse.stringToJson(response);
        ArrayList<String> propList = new ArrayList<>();

        for (int i = 0; i < props.length(); ++i) {
            try {
                JSONObject prop = props.getJSONObject(i);
                propList.add(prop.getString("formattedAddress"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return propList;
    }

    public static RentalProperty getPropertyData(String response, String address){
        JSONArray props = ConvertResponse.stringToJson(response);

        for (int i = 0; i < props.length(); ++i) {
            try {
                JSONObject prop = props.getJSONObject(i);

                if(address.equals(prop.getString("formattedAddress"))){
                    String city = prop.getString("city");
                    int bedroom = prop.getInt("bedrooms");
                    int bathroom = prop.getInt("bathrooms");
                    double price = prop.getDouble("price");

                    return new RentalProperty(city, address, bedroom, bathroom, price);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static double[] getLocFromName(String location, CityView cv){
        double latitude = 0.0, longitude = 0.0;
        // use google maps geocoder api to take input address and return the lat and long in doubles
        try {
            Geocoder geocoder = new Geocoder(cv, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(location, 5);
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new double[]{latitude, longitude};
    }

    // to toggle visibility of property details container easily
    public static void changeDetailsVisibility(Boolean visible, TextView propName, ImageView container, Button seeDetails){
        if(visible){
            // make all container components visible
            seeDetails.setVisibility(View.VISIBLE);
            propName.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
        } else {
            // make all container components invisible
            seeDetails.setVisibility(View.INVISIBLE);
            propName.setVisibility(View.INVISIBLE);
            container.setVisibility(View.INVISIBLE);
        }
    }
}