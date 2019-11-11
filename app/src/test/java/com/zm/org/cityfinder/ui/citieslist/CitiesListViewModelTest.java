package com.zm.org.cityfinder.ui.citieslist;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.zm.org.cityfinder.model.CitiesDataSource;
import com.zm.org.cityfinder.model.dto.CityData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CitiesListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Mock
    private CitiesDataSource citiesDataSource;

    @Mock
    private Observer<List<CityData>> observer;

    private CitiesListViewModel viewModel;
    private  List<CityData> cityDataList;

    @Before
    public void initMocks() {

        MockitoAnnotations.initMocks(this);
// Mock API response
        cityDataList = new ArrayList<>();
        cityDataList.add(new CityData(0, "Alabama", "US", 0, 0));
        cityDataList.add(new CityData(0, "Albuquerque", "US", 0, 0));
        cityDataList.add(new CityData(0, "Anaheim", "US", 0, 0));
        cityDataList.add(new CityData(0, "Arizona", "US", 0, 0));
        cityDataList.add(new CityData(0, "Sydney", "AU", 0, 0));

    }

    @Test
    public void testNull() {
        MutableLiveData citiesResponseListLiveData = new MutableLiveData();

        when(citiesDataSource.loadCities()).thenReturn(citiesResponseListLiveData);
        viewModel = new CitiesListViewModel(citiesDataSource);

        viewModel.cityDataListLiveData.observeForever(observer);

        assertNotNull(viewModel.cityDataListLiveData);
        assertTrue(viewModel.cityDataListLiveData.hasObservers());
    }

    /**
     * If the prefix given is "Alb" then the only result is "Albuquerque, US"
     */
    @Test
    public void testApiFetchDataSuccessSearch1() {

        MutableLiveData citiesResponseListLiveData = new MutableLiveData();

        when(citiesDataSource.loadCities()).thenReturn(citiesResponseListLiveData);
        viewModel = new CitiesListViewModel(citiesDataSource);
        viewModel.cityDataListLiveData.observeForever(observer);
        citiesResponseListLiveData.postValue(cityDataList);


        // fetched success
        verify(observer).onChanged(cityDataList);

        viewModel.searchQueryTextLiveData.postValue("alb");

        try {

            // add sleep for current thread until make sure Search Result thread finished
            Thread.sleep(50000l);

            List<CityData> filteredResult = new ArrayList<>();
            filteredResult.add(new CityData(0, "Albuquerque", "US", 0, 0));

            assertEquals(filteredResult.get(0).name, viewModel.cityDataListLiveData.getValue().get(0).name) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * If the given prefix is "Al", "Alabama, US" and "Albuquerque, US" are the only results.
     */
    @Test
    public void testApiFetchDataSuccessSearch2() {

        MutableLiveData citiesResponseListLiveData = new MutableLiveData();

        when(citiesDataSource.loadCities()).thenReturn(citiesResponseListLiveData);
        viewModel = new CitiesListViewModel(citiesDataSource);
        viewModel.cityDataListLiveData.observeForever(observer);
        citiesResponseListLiveData.postValue(cityDataList);


        // fetched success
        verify(observer).onChanged(cityDataList);

        viewModel.searchQueryTextLiveData.postValue("Al");

        try {

            // add sleep for current thread until make sure Search Result thread finished
            Thread.sleep(50000l);

            List<CityData> filteredResult = new ArrayList<>();
            filteredResult.add(new CityData(0, "Alabama", "US", 0, 0));
            filteredResult.add(new CityData(0, "Albuquerque", "US", 0, 0));
            assertTrue(filteredResult.equals(viewModel.cityDataListLiveData.getValue()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
      Contrariwise, if the given prefix is "s", the only result should be "Sydney, AU".
     */
    @Test
    public void testApiFetchDataSuccessSearch3() {

        MutableLiveData citiesResponseListLiveData = new MutableLiveData();

        when(citiesDataSource.loadCities()).thenReturn(citiesResponseListLiveData);
        viewModel = new CitiesListViewModel(citiesDataSource);
        viewModel.cityDataListLiveData.observeForever(observer);
        citiesResponseListLiveData.postValue(cityDataList);


        // fetched success
        verify(observer).onChanged(cityDataList);

        viewModel.searchQueryTextLiveData.postValue("s");

        try {

            // add sleep for current thread until make sure Search Result thread finished
            Thread.sleep(50000l);

            List<CityData> filteredResult = new ArrayList<>();
            filteredResult.add(new CityData(0, "Sydney", "AU", 0, 0));
            assertTrue(filteredResult.equals(viewModel.cityDataListLiveData.getValue()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}