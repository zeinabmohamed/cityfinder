package com.zm.org.cityfinder.ui.main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.zm.org.cityfinder.model.CitiesDataSource;
import com.zm.org.cityfinder.model.dto.CityData;

import java.util.List;

public class CitiesListViewModel extends ViewModel {

 public LiveData<List<CityData>> cityDataListLiveData;
    CitiesDataSource  citiesDataSource = new CitiesDataSource();

    public void loadCities(Context context) {

        cityDataListLiveData = citiesDataSource.loadCities(context);
    }
}
