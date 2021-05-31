package object;

import object.places.Location;

import java.util.ArrayList;

public class FieldDetect {
    Double minLat;
    Double maxLat;
    Double minLng;
    Double maxLng;

    Double centerLat;
    Double centerLng;

    Double distance;
    boolean isDetected = false;

    public FieldDetect(ArrayList<Location> listLocation) {
        if(listLocation == null || listLocation.isEmpty()){
            isDetected = false;
        }else {
            isDetected = true;
            Location firstLocation = listLocation.get(0);
            minLat = firstLocation.getLat();
            maxLat = firstLocation.getLat();
            minLng = firstLocation.getLng();
            maxLng = firstLocation.getLng();
            for(Location location : listLocation){
                minLat = Math.min(minLat,location.getLat());
                maxLat = Math.max(maxLat,location.getLat());
                minLng = Math.min(minLng,location.getLng());
                maxLng = Math.max(maxLng,location.getLng());
            }
            centerLat = (maxLat+minLat)/2;
            centerLng = (maxLng+minLng)/2;
            distance = distance(centerLat,maxLat,centerLng,maxLng,0,0);
        }
    }

    public Double getMinLat() {
        return minLat;
    }

    public void setMinLat(Double minLat) {
        this.minLat = minLat;
    }

    public Double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(Double maxLat) {
        this.maxLat = maxLat;
    }

    public Double getMinLng() {
        return minLng;
    }

    public void setMinLng(Double minLng) {
        this.minLng = minLng;
    }

    public Double getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(Double maxLng) {
        this.maxLng = maxLng;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Double getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(Double centerLng) {
        this.centerLng = centerLng;
    }

    public boolean isDetected() {
        return isDetected;
    }

    public void setDetected(boolean detected) {
        isDetected = detected;
    }

    public Double getDistance() {
        return distance;
    }

    public Location getCenterLocation(){
        if(centerLng == 0.0 || centerLat == 0.0){
            return null;
        }else {
            Location location = new Location(centerLat,centerLng);
            return location;
        }
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth(meter)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
