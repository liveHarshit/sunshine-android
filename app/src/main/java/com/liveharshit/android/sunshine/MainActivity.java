package com.liveharshit.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.liveharshit.android.sunshine.data.SunshinePreferences;
import com.liveharshit.android.sunshine.utilities.NetworkUtils;
import com.liveharshit.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<String []> {

    private TextView mWeatherTextView;
    private TextView errorTextView;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private static final int loaderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = (TextView)findViewById(R.id.tv_weather_data);
        errorTextView = (TextView)findViewById(R.id.error_text_view);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        mForecastAdapter = new ForecastAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mForecastAdapter);

        LoaderCallbacks<String[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            mForecastAdapter.setWeatherData(null);
            getSupportLoaderManager().restartLoader(loaderId, null, this);

            return true;
        }
        if (id==R.id.show_map_location) {
            openLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLocationInMap() {
        String location = SunshinePreferences.DEFAULT_MAP_LOCATION;
        Uri geoLocation = Uri.parse("geo:0,0?q=" + location);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {


        return new AsyncTaskLoader<String[]>(this) {

            String[] mWeatherData = null;
            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    deliverResult(mWeatherData);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    errorTextView.setVisibility(View.INVISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                String locationQuery = SunshinePreferences
                        .getPreferredWeatherLocation(MainActivity.this);

                URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);

                try {
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);

                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                            .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                    return simpleJsonWeatherData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] strings) {
        progressBar.setVisibility(View.INVISIBLE);
        if (null == strings) {
            errorTextView.setVisibility(View.VISIBLE);
        } else {
            errorTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

}
