package com.zm.org.cityfinder.model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
    public MutableLiveData<List<CityData>> loadCities(final Context context) {

        final MutableLiveData<List<CityData>> citiesListResponseLiveData = new MutableLiveData();
        new AsyncTask<Void, Void, List<CityData>>() {

            @Override
            protected List<CityData> doInBackground(Void... voids) {
                try {
                    InputStream inputStream = context.getAssets().open("cities.json");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    String citiesJson = new String(buffer);
                    Type listType = new TypeToken<List<CityData>>() {}.getType();
                    List<CityData> cityDataList = new Gson().fromJson(citiesJson,listType);

                    return cityDataList;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<CityData> data) {
                Toast.makeText(context,"data "+data.size(),Toast.LENGTH_SHORT).show();
                citiesListResponseLiveData.setValue(data);
            }
        }.execute();


        return citiesListResponseLiveData;
    }
}
