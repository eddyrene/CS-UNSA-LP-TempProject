package com.example.alquiler.alquilercom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Main activity.
 */
public class MainActivity_slider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slider);
        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction()
              //      .add(R.id.container, MainFragment.instance())
                //    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SimpleViewsFragment.instance())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
