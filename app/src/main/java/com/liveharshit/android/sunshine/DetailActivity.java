package com.liveharshit.android.sunshine;

import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView mWeatherOfTheDay;
    private String weatherOfTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mWeatherOfTheDay = (TextView) findViewById(R.id.tv_display_weather);

        Intent intent = getIntent();
        if(intent !=null) {
            if(intent.hasExtra(Intent.EXTRA_TEXT)) {
                weatherOfTheDay = intent.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherOfTheDay.setText(weatherOfTheDay);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Log.d("Share icon clicked","True");
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra("sms_body", weatherOfTheDay);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
