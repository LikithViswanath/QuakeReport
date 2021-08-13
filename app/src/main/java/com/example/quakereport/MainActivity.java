
package com.example.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    ArrayList<Earthquake> earthquakes;
    EarthquakeAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Adapter = new EarthquakeAdapter(this, 0, new ArrayList<>() );
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(Adapter);

        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            getLoaderManager().initLoader(0, null, this);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                if (earthquakes != null) {
                    String url = earthquakes.get(position).getmUrl();
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        }
        else
        {
            ProgressBar LoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
            LoadingSpinner.setVisibility(View.GONE);
            listView.setEmptyView(findViewById(R.id.InternetConnectivity));
        }
    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {
        ProgressBar LoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        LoadingSpinner.setVisibility(View.GONE);
        if(data==null) {
            ListView listView = findViewById(R.id.list_view);
            listView.setEmptyView(findViewById(R.id.emptyElement));
        }
        else
        {
            Adapter.addEarthquakes(data);
            earthquakes = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {

    }

    static private class EarthquakeLoader extends  AsyncTaskLoader<ArrayList<Earthquake>>{

        URL url = null;
        String JsonResponse="";
        public EarthquakeLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public ArrayList<Earthquake> loadInBackground() {
            QueryUtils extract_data = new QueryUtils();
            try {
                url = extract_data.getURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            finally {

                if(url!=null){
                    try {
                        JsonResponse = extract_data.makeHttpConnection(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return extract_data.extractEarthquakes(JsonResponse);
        }
    }
}
