package com.example.quakereport;

public class Earthquake {
       private final Double mMag;
       private final String mGeoLocation;
       private final long mDate;
       private String mUrl;
       public Earthquake(Double Mag,String GeoLocation,long date,String Url){
           mMag=Mag;
           mGeoLocation=GeoLocation;
           mDate=date;
           mUrl=Url;
       }
    public long getmDate() {
        return mDate;
    }
    public Double getmMag() {
        return mMag;
    }
    public String getGeoLocation() {
        return mGeoLocation;
    }
    public  String getmUrl(){
           return mUrl;
    }
}
