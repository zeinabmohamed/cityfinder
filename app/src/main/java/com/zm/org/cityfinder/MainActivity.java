package com.zm.org.cityfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.zm.org.cityfinder.ui.citieslist.CitiesListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CitiesListFragment.newInstance())
                    .commit();
        }
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
