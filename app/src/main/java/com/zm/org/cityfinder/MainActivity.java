package com.zm.org.cityfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.MenuItem;

import com.zm.org.cityfinder.model.dto.CityData;
import com.zm.org.cityfinder.ui.citieslist.CitiesListFragment;
import com.zm.org.cityfinder.ui.citieslist.CitiesListViewModel;
import com.zm.org.cityfinder.ui.map.MapFragment;

public class MainActivity extends AppCompatActivity {


    private CitiesListViewModel citiesListViewModel;
    private ViewModelProvider.Factory citiesListViewModelFactory =  new CitiesListViewModel.CitiesListViewModelFactory(this);

    private boolean isLandScapeMode = true;

    public boolean isLandScapeMode() {
        return isLandScapeMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        citiesListViewModel = ViewModelProviders.of(this,citiesListViewModelFactory).get(CitiesListViewModel.class);

        // Check whether the activity is using the layout version with
        // the container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.mainContainer) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.

             getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, CitiesListFragment.newInstance())
                        .commit();
             isLandScapeMode = false;
        }

        citiesListViewModel.citySelected.observe(this, new Observer<CityData>() {
                @Override
                public void onChanged(CityData cityData) {
                    // navigate to MapFragment
                    if (!isLandScapeMode) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainContainer, MapFragment.newInstance(cityData))
                                .addToBackStack(null)
                                .commit();
                    }else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mapConatiner, MapFragment.newInstance(cityData))
                                .commit();
                    }
                }
            });

    }

    public void showBackButton(boolean showBackButton) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
    }

    public void updateTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
          showBackButton(true);
        }else {
            // initial screen >> city list
            updateTitle(getString(R.string.type_city_name));
            showBackButton(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
