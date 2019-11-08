package com.zm.org.cityfinder.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ListAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zm.org.cityfinder.R;
import com.zm.org.cityfinder.databinding.CitiesListFragmentBinding;
import com.zm.org.cityfinder.model.dto.CityData;

import java.util.List;

public class CitiesListFragment extends Fragment {

    private CitiesListViewModel  citiesListViewModel;
    private CitiesListFragmentBinding binding;

    public static CitiesListFragment newInstance() {
        return new CitiesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(inflater, R.layout.cities_list_fragment , container, false);
        binding.setViewModel(citiesListViewModel);
        binding.setAdapter(new CitiesListAdapter());
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        citiesListViewModel = ViewModelProviders.of(this).get(CitiesListViewModel.class);
        citiesListViewModel.loadCities(getContext());

        citiesListViewModel.cityDataListLiveData.observe(this, new Observer<List<CityData>>() {
            @Override
            public void onChanged(List<CityData> cityData) {
                Log.d("data","cityData "+cityData.size());
                ((ListAdapter)  binding.citiesRecyclerView.getAdapter()).submitList(cityData);
                binding.citiesRecyclerView.getAdapter().notifyDataSetChanged();

            }
        });

    }

}
