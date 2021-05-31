package object.result;

import com.google.gson.annotations.SerializedName;

public class RequestBodyAddPlace{

	@SerializedName("street")
	private String street;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("name")
	private String name;

	@SerializedName("street_number")
	private String streetNumber;

	@SerializedName("type")
	private String type;

	@SerializedName("longitude")
	private String longitude;

	public void setStreet(String street){
		this.street = street;
	}

	public String getStreet(){
		return street;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setStreetNumber(String streetNumber){
		this.streetNumber = streetNumber;
	}

	public String getStreetNumber(){
		return streetNumber;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}
}