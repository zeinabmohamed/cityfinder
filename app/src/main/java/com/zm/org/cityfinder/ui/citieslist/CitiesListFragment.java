package com.zm.org.cityfinder.ui.citieslist;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zm.org.cityfinder.MainActivity;
import com.zm.org.cityfinder.R;
import com.zm.org.cityfinder.databinding.CitiesListFragmentBinding;
import com.zm.org.cityfinder.model.dto.CityData;

import java.util.List;

public class CitiesListFragment extends Fragment implements CitiesListAdapter.OnItemClickListener {

    private CitiesListViewModel citiesListViewModel;
    private CitiesListFragmentBinding binding;

    public static CitiesListFragment newInstance() {
        return new CitiesListFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateHeader();
    }


    private void updateHeader() {
        ((MainActivity) getActivity()).updateTitle(getString(R.string.type_city_name));
        ((MainActivity) getActivity()).showBackButton(false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        citiesListViewModel = ViewModelProviders.of(getActivity()).get(CitiesListViewModel.class);
        citiesListViewModel.loadCities(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.cities_list_fragment, container, false);
        binding.setViewModel(citiesListViewModel);
        binding.setAdapter(new CitiesListAdapter(this));
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        citiesListViewModel.cityDataListLiveData.observe(getViewLifecycleOwner(), new Observer<List<CityData>>() {
            @Override
            public void onChanged(List<CityData> cityData) {
                Log.d("data", "cityData " + cityData.size());
                // just work around to force update the adapter list as mention here
                ((CitiesListAdapter) binding.citiesRecyclerView.getAdapter()).submitList(null);
                // update adapter with list
                ((CitiesListAdapter) binding.citiesRecyclerView.getAdapter()).submitList(cityData);

                binding.citiesRecyclerView.getAdapter().notifyDataSetChanged();
                if (((MainActivity)getActivity()).isLandScapeMode()) {
                    // handle in landscape mode show inflate mapFragment  with 1'st city
                    // or last selected city if exist
                    if(citiesListViewModel.citySelected.getValue() == null) {
                        citiesListViewModel.citySelected.postValue(cityData.get(0));
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(CityData cityData) {
        citiesListViewModel.citySelected.postValue(cityData);
    }
}
