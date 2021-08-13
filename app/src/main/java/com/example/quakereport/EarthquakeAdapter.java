package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

      ArrayList<Earthquake> earthquakes;
    public EarthquakeAdapter(Context context, int resource, ArrayList<Earthquake> earthquakes) {
        super(context,0,earthquakes);
        this.earthquakes=earthquakes;
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        View activity = convertView;
        if(activity==null){
            activity= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Earthquake currentQuake = getItem(position);
        TextView magTextView = (TextView) activity.findViewById(R.id.magnitude);
        TextView DirectionTextView = (TextView) activity.findViewById(R.id.location_offset);
        TextView countryTextView = (TextView) activity.findViewById(R.id.primary_location);
        TextView dateTextView = (TextView) activity.findViewById(R.id.date);
        TextView timeTextView = (TextView) activity.findViewById(R.id.time);

        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentQuake.getmMag());
        magnitudeCircle.setColor(magnitudeColor);

        magTextView.setText(currentQuake.getmMag().toString());
        String geoLocation = currentQuake.getGeoLocation();
        int i = geoLocation.indexOf('f');
        String Direction = geoLocation.substring(0,i+1);
        String countryLocation = geoLocation.substring(i+1,geoLocation.length());
        DirectionTextView.setText(Direction);
        countryTextView.setText(countryLocation);

        long time = currentQuake.getmDate();
        Date dateObject = new Date(time);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
        String dateToDisplay1 = dateFormatter.format(dateObject);
        dateTextView.setText(dateToDisplay1);

        dateFormatter = new SimpleDateFormat("h:mm a");
        String dateToDisplay2 = dateFormatter.format(dateObject);
        timeTextView.setText(dateToDisplay2);
        return  activity;
    }
    void addEarthquakes( ArrayList<Earthquake> data ){
        earthquakes.addAll(data);
        notifyDataSetChanged();
    }
}
