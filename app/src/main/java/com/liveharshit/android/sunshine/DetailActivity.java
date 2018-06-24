package com.liveharshit.android.sunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView mWeatherOfTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mWeatherOfTheDay = (TextView) findViewById(R.id.tv_display_weather);

        Intent intent = getIntent();
        if(intent !=null) {
            if(intent.hasExtra(Intent.EXTRA_TEXT)) {
                String weatherOfTheDay = intent.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherOfTheDay.setText(weatherOfTheDay);
            }
        }


    }
}
