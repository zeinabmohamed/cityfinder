package com.zm.org.cityfinder.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zm.org.cityfinder.R;
import com.zm.org.cityfinder.databinding.CitiesListFragmentBinding;

public class CitiesListFragment extends Fragment {

    private CitiesListViewModel  citiesListViewModel;

    public static CitiesListFragment newInstance() {
        return new CitiesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        CitiesListFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.cities_list_fragment , container, false);
        binding.setViewModel(citiesListViewModel);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        citiesListViewModel = ViewModelProviders.of(this).get(CitiesListViewModel.class);
        citiesListViewModel.loadCities(getContext());

    }

}
