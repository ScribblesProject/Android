/**
 * Created by Alejandro on 10/24/2016.
 * Class of coordinates for the assets
 */

public class AssetCoordinates {
    private double longitude;
    private double latitude;

    public AssetCoordinates(){
        longitude = 0;
        latitude = 0;
    }

    public AssetCoordinates(double assetLong, double assetLat){
        longitude = assetLong;
        latitude = assetLat;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double assetLong){
        longitude = assetLong;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double assetLat){
        latitude = assetLat;
    }
}
