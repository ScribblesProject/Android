package com.scribblesinc.tams.androidcustom;

/**
 * Created by Joel on 10/19/2016.
 */

public class Locations {
        private double latitude;
        private double longitude;

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
            return latitude + "," + longitude;
        }
}
