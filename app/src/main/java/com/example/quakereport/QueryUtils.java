package com.example.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public final class QueryUtils {

    private static final String LOG_TAG = "MyActivity";

    QueryUtils(){
    }

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=30";

    public URL getURL() throws MalformedURLException {
        URL url = null;
        url = new URL(USGS_REQUEST_URL);
        return url;
    }

    public String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder Output = new StringBuilder();
        if(inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String Line = reader.readLine();
            while (Line!=null)
            {
                Output.append(Line);
                Line=reader.readLine();
            }
        }
        return Output.toString();
    }

    public String makeHttpConnection( URL url ) throws IOException{
        String JsonResponse="";
        if(url==null) return JsonResponse;
        HttpURLConnection UrlConnection = null;
        InputStream inputStream = null;
        try
        {
            UrlConnection = (HttpURLConnection) url.openConnection();
            UrlConnection.setReadTimeout(1000);
            UrlConnection.setConnectTimeout(15000);
            UrlConnection.setRequestMethod("GET");
            UrlConnection.connect();

            if(UrlConnection.getResponseCode()==200) {
                inputStream = UrlConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + UrlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (UrlConnection != null) UrlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return  JsonResponse;
    }

    public static ArrayList<Earthquake> extractEarthquakes(String jsonResponse) {

        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(jsonResponse);
            JSONArray feature=reader.optJSONArray("features");
            for(int i=0;i<feature.length();i++)
            {
                JSONObject properties = feature.getJSONObject(i);
                JSONObject type = properties.getJSONObject("properties");

                Double mag = type.getDouble("mag");
                String county =  type.getString("place");
                long date = type.getLong("time");
                String url = type.getString("url");

                Earthquake earthquake = new Earthquake(mag,county,date,url);
                earthquakes.add(earthquake);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return earthquakes;
    }

}
