package com.zm.org.cityfinder.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zm.org.cityfinder.model.CitiesDataSource;
import com.zm.org.cityfinder.model.dto.CityData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class CitiesListViewModel extends ViewModel {

    public SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            searchQueryTextLiveData.postValue(newText);

            return true;
        }
    };

    public MediatorLiveData<List<CityData>> cityDataListLiveData;
    public MutableLiveData<List<CityData>> citiesListResponseLiveData;

    CitiesDataSource citiesDataSource = new CitiesDataSource();
    private MutableLiveData<String> searchQueryTextLiveData = new MutableLiveData();
    private String lastSearchQuery = ""; // initial state  empty search

    public CitiesListViewModel() {
        cityDataListLiveData = new MediatorLiveData();
    }

    public void loadCities(final Context context) {

        citiesListResponseLiveData = new MutableLiveData<>();

        citiesDataSource.loadCities(citiesListResponseLiveData, context);
        cityDataListLiveData.addSource(citiesListResponseLiveData, new Observer<List<CityData>>() {
            @Override
            public void onChanged(List<CityData> cityData) {
                Log.i("data", "citiesListResponseLiveData : " + cityData.size());
                cityDataListLiveData.postValue(cityData);
            }
        });

        cityDataListLiveData.addSource(searchQueryTextLiveData, new Observer<String>() {
            @Override
            public void onChanged(final String searchQueryText) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("data", "start searchQueryTextLiveData  : searchQueryText " + searchQueryText);

                        if (!searchQueryText.equals(lastSearchQuery)) {
                            List citiesList = new ArrayList(citiesListResponseLiveData.getValue());

                            ListIterator<CityData> cityDataIterator = citiesList.listIterator();
                            while (cityDataIterator.hasNext()) {

                                CityData cityItem = cityDataIterator.next();
                                if (!cityItem.name.startsWith(searchQueryText) && !cityItem.country.startsWith(searchQueryText)) {
                                    cityDataIterator.remove();
                                }
                            }
                            Log.i("data", "end searchQueryTextLiveData  : searchQueryText " + searchQueryText);

                            lastSearchQuery = searchQueryText;
                            cityDataListLiveData.postValue(citiesList);
                        }
                    }
                }).start();
            }
        });
    }
}
