package com.zm.org.cityfinder.model;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zm.org.cityfinder.model.dto.CityData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class CitiesDataSource {

    public void loadCities(final MutableLiveData<LinkedList<CityData>> citiesListResponseLiveData, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = context.getAssets().open("cities.json");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    String citiesJson = new String(buffer);
                    Type listType = new TypeToken<LinkedList<CityData>>() {
                    }.getType();
                    LinkedList<CityData> cityDataList = new Gson().fromJson(citiesJson, listType);

                    Log.i("data", "Load json cities " + cityDataList.size());
                    // sort data city name 1'st then city country 2'nd

                    Collections.sort(cityDataList, new Comparator<CityData>() {
                        @Override
                        public int compare(CityData cityData0, CityData cityData1) {

                            return (cityData0.name.concat(cityData0.country)).compareToIgnoreCase((cityData1.name.concat(cityData1.country)));
                        }
                    });

                    citiesListResponseLiveData.postValue(cityDataList);

                } catch (IOException e) {
                    e.printStackTrace();
                    citiesListResponseLiveData.postValue(null);
                }
            }
        }).start();


    }
}
