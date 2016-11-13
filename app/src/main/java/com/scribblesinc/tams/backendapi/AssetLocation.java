package com.scribblesinc.tams.backendapi;

/**
 * Created by danielmj on 11/10/16.
 */

public class AssetLocation {
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
}