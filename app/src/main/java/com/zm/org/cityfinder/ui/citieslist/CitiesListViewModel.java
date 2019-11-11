package com.zm.org.cityfinder.ui.citieslist;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zm.org.cityfinder.model.CitiesDataSource;
import com.zm.org.cityfinder.model.dto.CityData;

import java.util.LinkedList;
import java.util.List;

public class CitiesListViewModel extends ViewModel {

    public MediatorLiveData<List<CityData>> cityDataListLiveData = new MediatorLiveData();
    public MutableLiveData<CityData> citySelected =  new MutableLiveData<>();
    private LiveData<List<CityData>> citiesListResponseLiveData ;

    public MutableLiveData<String> searchQueryTextLiveData = new MutableLiveData();
    public MutableLiveData<Integer> progressBarVisibilityLiveData = new MutableLiveData();

    private String TAG = CitiesListViewModel.class.getSimpleName();
    public CitiesListViewModel(CitiesDataSource citiesDataSource) {

        if(citiesListResponseLiveData == null) {
            progressBarVisibilityLiveData.postValue(View.VISIBLE);
            citiesListResponseLiveData =   citiesDataSource.loadCities();
        }

        cityDataListLiveData.addSource(citiesListResponseLiveData, new Observer<List<CityData>>() {
            @Override
            public void onChanged(List<CityData> cityData) {
                Log.d(TAG, "citiesListResponseLiveData : " + cityData.size());
                updateViewWithData(cityData);
            }
        });
        cityDataListLiveData.addSource(searchQueryTextLiveData, new Observer<String>() {
            @Override
            public void onChanged(final String searchQueryText) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "start  searchQueryText " + searchQueryText);
                        if (citiesListResponseLiveData.getValue() != null &&
                                !citiesListResponseLiveData.getValue().isEmpty()) {

                            if (searchQueryText.isEmpty()) {
                                // should update with whole list
                                updateViewWithData(citiesListResponseLiveData.getValue());

                                return;
                            }

                            // as long we sort the data we don't need to check all of the data
                            // so we will cut the list from the starting index of searchTextQuery
                            CityData cityData = new CityData(searchQueryText);

                            List citiesList = new LinkedList<CityData>();

                            int startIndex = citiesListResponseLiveData.getValue().indexOf(cityData);
                            Log.d(TAG, "start  startIndex " + startIndex);

                            // if not success to get filtered result will empty list
                            if (startIndex == -1) {
                                updateViewWithData(citiesList);
                                return;
                            }
                            citiesList = citiesListResponseLiveData.getValue().subList(startIndex, citiesListResponseLiveData.getValue().size() - 1);
                            if(citiesList.size() ==0){
                                 citiesList.add(citiesListResponseLiveData.getValue().get(startIndex));
                                updateViewWithData(citiesList);
                                return;
                            }
                            int lastIndex = citiesList.lastIndexOf(cityData);

                            Log.d(TAG, "end  lastIndex " + lastIndex);

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


    public SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            //Do something after 100ms
            Log.d(TAG, "onQueryTextChange: " + newText);
            searchQueryTextLiveData.postValue(newText);
            return true;
        }
    };


    private void updateViewWithData(List citiesList) {
        cityDataListLiveData.postValue(citiesList);
        progressBarVisibilityLiveData.postValue(View.GONE);
    }


    public static class CitiesListViewModelFactory implements ViewModelProvider.Factory{

        private Context context;

        public CitiesListViewModelFactory(Context context){

            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CitiesListViewModel( new CitiesDataSource(context));
        }
    }
}
