package com.zm.org.cityfinder.ui.map;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zm.org.cityfinder.MainActivity;
import com.zm.org.cityfinder.R;
import com.zm.org.cityfinder.databinding.MapFragmentBinding;
import com.zm.org.cityfinder.model.dto.CityData;
import com.zm.org.cityfinder.ui.about.AboutActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private static final String CITY_DATA_KEY = "city_data_key";
    private CityData cityData;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(CityData cityData) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(CITY_DATA_KEY,cityData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityData = (CityData) getArguments().getSerializable(CITY_DATA_KEY);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateHeader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MapFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);
        binding.mapView.onResume();
        binding.aboutInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAboutInfo();
            }
        });
        return binding.getRoot();
    }

    private void updateHeader() {
        if(cityData != null && !TextUtils.isEmpty(cityData.name)){
            ((MainActivity)getActivity() ).updateTitle(cityData.name);
        }else{
            ((MainActivity)getActivity() ).updateTitle(getString(R.string.city_coord));
        }
        ((MainActivity)getActivity() ).showBackButton(true);
    }

    private void navigateToAboutInfo() {
        startActivity(new Intent(getContext(), AboutActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng cityCoordLatLng =  new LatLng(cityData.coord.lat, cityData.coord.lon);
        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                cityCoordLatLng).title(cityData.name);

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(cityCoordLatLng).zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
