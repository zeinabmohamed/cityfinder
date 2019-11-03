package com.zm.org.cityfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zm.org.cityfinder.ui.main.CitiesListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CitiesListFragment.newInstance())
                    .commitNow();
        }
    }
}
