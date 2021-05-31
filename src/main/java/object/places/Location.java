package object.places;

import com.google.gson.annotations.SerializedName;

public class Location{

	@SerializedName("lng")
	private double lng;

	@SerializedName("lat")
	private double lat;

	public Location(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public double getLng(){
		return lng;
	}

	public double getLat(){
		return lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String toStringForGcs(){
		StringBuilder latlngStr = new StringBuilder();
		latlngStr.append(String.format("%.7f,%.7f",lat,lng));
		return latlngStr.toString();
	}

}