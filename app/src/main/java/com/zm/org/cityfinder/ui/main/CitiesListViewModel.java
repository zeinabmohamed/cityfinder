package com.zm.org.cityfinder.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.zm.org.cityfinder.model.CitiesDataSource;
import com.zm.org.cityfinder.model.dto.CityData;

import java.util.List;

public class CitiesListViewModel extends ViewModel {

    public MediatorLiveData<List<CityData>> cityDataListLiveData;
    public MutableLiveData<List<CityData>> citiesListResponseLiveData;

    CitiesDataSource citiesDataSource = new CitiesDataSource();

    public CitiesListViewModel() {
        cityDataListLiveData = new MediatorLiveData();
    }

    public void loadCities(Context context) {

        citiesListResponseLiveData  = new MutableLiveData<>();

        citiesDataSource.loadCities(citiesListResponseLiveData,  context);
        cityDataListLiveData.addSource(citiesListResponseLiveData, new Observer<List<CityData>>() {
            @Override
            public void onChanged(List<CityData> cityData) {
                Log.i("data", "citiesListResponseLiveData : " + cityData.size());
                cityDataListLiveData.postValue(cityData);

            }
        });
    }
}
