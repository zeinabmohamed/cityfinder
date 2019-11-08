package com.zm.org.cityfinder.model;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zm.org.cityfinder.model.dto.CityData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class CitiesDataSource {

    public void loadCities(final MutableLiveData<List<CityData>> citiesListResponseLiveData, final Context context) {
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
                    Type listType = new TypeToken<List<CityData>>() {
                    }.getType();
                    List<CityData> cityDataList = new Gson().fromJson(citiesJson, listType);

                    Log.i("data ", "Thread " + cityDataList.size());
                    citiesListResponseLiveData.postValue(cityDataList);

                } catch (IOException e) {
                    e.printStackTrace();
                    citiesListResponseLiveData.postValue(null);
                }
            }
        }).start();


    }
}
