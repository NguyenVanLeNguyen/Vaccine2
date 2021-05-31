package object.nodes;

import com.google.gson.annotations.SerializedName;

public class NodesItem{

	@SerializedName("street")
	private String street;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("name")
	private String name;

	@SerializedName("street_number")
	private String streetNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("source")
	private Object source;

	@SerializedName("type")
	private int type;

	@SerializedName("google_type")
	private Object googleType;

	@SerializedName("longitude")
	private double longitude;

	public String getStreet(){
		return street;
	}

	public double getLatitude(){
		return latitude;
	}

	public String getName(){
		return name;
	}

	public String getStreetNumber(){
		return streetNumber;
	}

	public int getId(){
		return id;
	}

	public Object getSource(){
		return source;
	}

	public int getType(){
		return type;
	}

	public Object getGoogleType(){
		return googleType;
	}

	public double getLongitude(){
		return longitude;
	}
}