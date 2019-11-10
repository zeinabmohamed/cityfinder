package com.zm.org.cityfinder.ui.citieslist;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.zm.org.cityfinder.model.CitiesDataSource;
import com.zm.org.cityfinder.model.dto.CityData;

import java.util.LinkedList;
import java.util.List;

public class CitiesListViewModel extends ViewModel {

    public MediatorLiveData<List<CityData>> cityDataListLiveData = new MediatorLiveData();
    public MutableLiveData<CityData> citySelected =  new MutableLiveData<>();
    private MutableLiveData<LinkedList<CityData>> citiesListResponseLiveData = new MutableLiveData<>();

    private CitiesDataSource citiesDataSource = new CitiesDataSource();
    private MutableLiveData<String> searchQueryTextLiveData = new MutableLiveData();
    public MutableLiveData<Integer> progressBarVisibilityLiveData = new MutableLiveData();

    public CitiesListViewModel() {


        cityDataListLiveData.addSource(citiesListResponseLiveData, new Observer<LinkedList<CityData>>() {
            @Override
            public void onChanged(LinkedList<CityData> cityData) {
                Log.i("data", "citiesListResponseLiveData : " + cityData.size());
                updateViewWithData(cityData);

            }
        });
        cityDataListLiveData.addSource(searchQueryTextLiveData, new Observer<String>() {
            @Override
            public void onChanged(final String searchQueryText) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("data", "start  searchQueryText " + searchQueryText);
                        if (citiesListResponseLiveData.getValue() != null &&
                                !citiesListResponseLiveData.getValue().isEmpty()) {

                            if (searchQueryText.isEmpty()) {
                                // should update with whole list
                                updateViewWithData(citiesListResponseLiveData.getValue());

                                return;
                            }

                            // as long we sort the data we don't need to check all of the data
                            // so we will cut the list from the starting index of searchTextQuery
                            CityData cityData = new CityData();
                            cityData.name = searchQueryText;

                            List citiesList = new LinkedList<CityData>();

                            int startIndex = citiesListResponseLiveData.getValue().indexOf(cityData);
                            Log.i("data", "start  startIndex " + startIndex);

                            // if not success to get filtered result will empty list
                            if (startIndex == -1) {
                                updateViewWithData(citiesList);
                                return;
                            }
                            citiesList = citiesListResponseLiveData.getValue().subList(startIndex, citiesListResponseLiveData.getValue().size() - 1);
                            int lastIndex = citiesList.lastIndexOf(cityData);

                            Log.i("data", "end  lastIndex " + lastIndex);

                            // if not success to get filtered result will empty list
                            if (lastIndex == -1) {
                                updateViewWithData(citiesList);
                                return;
                            }

                            // we add +1 for last index to include this item in filtered result
                            citiesList = citiesList.subList(0, lastIndex + 1);

                            updateViewWithData(citiesList);

                        }
                    }
                }).start();
            }
        });
    }


    public void loadCities(Context context) {
        if(citiesListResponseLiveData.getValue() == null) {
            progressBarVisibilityLiveData.postValue(View.VISIBLE);
            citiesDataSource.loadCities(citiesListResponseLiveData,context);
        }
    }
    public SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            //Do something after 100ms
            Log.i("data", "onQueryTextChange: " + newText);
            searchQueryTextLiveData.postValue(newText);
            return true;
        }
    };


    private void updateViewWithData(List citiesList) {
        cityDataListLiveData.postValue(citiesList);
        progressBarVisibilityLiveData.postValue(View.GONE);
    }

}
