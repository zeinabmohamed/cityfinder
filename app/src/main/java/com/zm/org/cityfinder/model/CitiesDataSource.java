package com.zm.org.cityfinder.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.zm.org.cityfinder.model.dto.CityData;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CitiesDataSource {

    private  WeakReference<MutableLiveData> citiesListResponseLiveData;
    private WeakReference<Context> context;
    private String Charset_UTF_8 = "UTF-8";
    private String TAG =  CitiesDataSource.class.getSimpleName();

    public CitiesDataSource(Context context) {

        this.context = new WeakReference(context);
         citiesListResponseLiveData = new WeakReference<>(new MutableLiveData());

    }

    public LiveData<List<CityData>> loadCities() {
            new AsyncTask<Void, Void, List<CityData>>() {
                @Override
                protected List<CityData> doInBackground(Void... voids) {
                    try {
                        InputStream inputStream = context.get().getAssets().open("cities.json");
                        // Enhance Json parsing time and memory consumption
                        // by using same stream while reading to start parse for time enhancement
                        // and map city JsonObject to custom flat CitData object  for memory enhancement
                        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, Charset_UTF_8));

                        // i choose linkedList as even it cost more memory but will be more efficient with search
                        // responding time and memory as it will not recreate new array but just reference
                        // in the new head of the target search result
                        LinkedList<CityData> cityDataList =  new LinkedList<>();

                        reader.beginArray();

                        while (reader.hasNext()){
                            Map<String,Object> map = new Gson().fromJson(reader, Map.class);
                            int id =  ((Double) map.get("_id")).intValue();
                            String country = (String) map.get("country");
                            String name = (String) map.get("name");
                            Map coord = (Map) map.get("coord");
                            double lon = (double) coord.get("lon");
                            double lat = (double) coord.get("lat");
                            cityDataList.add(new CityData(id,name,country,lon,lat));
                        }

                        reader.endArray();
                        inputStream.close();

                        Log.i("data", "Load json cities " + cityDataList.size());

                        // sort data city name 1'st then city country 2'nd

                        Collections.sort(cityDataList, new Comparator<CityData>() {
                            @Override
                            public int compare(CityData cityData0, CityData cityData1) {
                                return (cityData0.name.concat(cityData0.country)).compareToIgnoreCase((cityData1.name.concat(cityData1.country)));
                            }
                        });

                        return cityDataList;
                    } catch (IOException ex) {
                        Log.e(TAG, "doInBackground: error ",ex );
                    }
                    return Collections.emptyList();
                }

                @Override
                protected void onPostExecute(List<CityData> cityData) {
                    super.onPostExecute(cityData);
                    citiesListResponseLiveData.get().setValue(cityData);
                }
            }.execute();


        return citiesListResponseLiveData.get();
    }

}



