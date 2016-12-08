package com.scribblesinc.tams.backendapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by danielmj on 11/10/16.
 */

public class AssetLocation implements Parcelable {
    private double latitude;
    private double longitude;

    public AssetLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "AssetLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    private AssetLocation(Parcel in){
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<AssetLocation> CREATOR = new Parcelable.Creator<AssetLocation>(){
        public AssetLocation createFromParcel(Parcel in){
            return new AssetLocation(in);
        }
        public AssetLocation[] newArray(int size){
            return new AssetLocation[size];
        }
    };
}