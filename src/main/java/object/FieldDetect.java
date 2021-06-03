package object;

import object.mark.MarkPoint;
import object.places.Location;

import java.util.ArrayList;

public class FieldDetect {
    double minLat;
    double maxLat;
    double minLng;
    double maxLng;

    double centerLat;
    double centerLng;
    ArrayList<MarkPoint> mListPoint = new ArrayList<>();
    double distance;
    boolean isDetected = false;

    public FieldDetect(ArrayList<Location> listLocation, int id) {
        if(listLocation == null || listLocation.isEmpty()){
            isDetected = false;
        }else {
            isDetected = true;
            Location firstLocation = listLocation.get(0);
            minLat = firstLocation.getLat();
            maxLat = firstLocation.getLat();
            minLng = firstLocation.getLng();
            maxLng = firstLocation.getLng();
            int countPoint = listLocation.size();
            int indexPoint = 0;
            int hashNumber = 1;
            if (countPoint <= 3){
                hashNumber = 1;
            }else if(countPoint <= 6){
                hashNumber = 2;
            }else if(countPoint <= 10){
                hashNumber = 3;
            }else {
                hashNumber = 5;
            }
            for(Location location : listLocation){
                if(indexPoint%hashNumber == 0){
                    mListPoint.add(new MarkPoint(location,id));
                }
                indexPoint++;

            }
            centerLat = (maxLat+minLat)/2;
            centerLng = (maxLng+minLng)/2;
            //distance = distance(centerLat,maxLat,centerLng,maxLng,0,0);
        }



    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLng() {
        return minLng;
    }

    public void setMinLng(double minLng) {
        this.minLng = minLng;
    }

    public double getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(double maxLng) {
        this.maxLng = maxLng;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public double getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(double centerLng) {
        this.centerLng = centerLng;
    }

    public boolean isDetected() {
        return isDetected;
    }

    public void setDetected(boolean detected) {
        isDetected = detected;
    }

    public double getDistance() {
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

    /*public static double distance(double lat1, double lat2, double lon1,
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
    }*/

    public ArrayList<MarkPoint> getmListPoint() {
        return mListPoint;
    }

    public void setmListPoint(ArrayList<MarkPoint> mListPoint) {
        this.mListPoint = mListPoint;
    }
}
