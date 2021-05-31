package object.places;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResultsItem{

	@SerializedName("reference")
	private String reference;

	@SerializedName("types")
	private List<String> types;

	@SerializedName("scope")
	private String scope;

	@SerializedName("icon")
	private String icon;

	@SerializedName("name")
	private String name;

	@SerializedName("geometry")
	private Geometry geometry;

	@SerializedName("vicinity")
	private String vicinity;

	@SerializedName("photos")
	private List<PhotosItem> photos;

	@SerializedName("place_id")
	private String placeId;

	@SerializedName("business_status")
	private String businessStatus;

	@SerializedName("opening_hours")
	private OpeningHours openingHours;

	@SerializedName("plus_code")
	private PlusCode plusCode;

	@SerializedName("rating")
	private double rating;

	@SerializedName("user_ratings_total")
	private int userRatingsTotal;

	@SerializedName("permanently_closed")
	private boolean permanentlyClosed;

	public String getReference(){
		return reference;
	}

	public List<String> getTypes(){
		return types;
	}

	public String getScope(){
		return scope;
	}

	public String getIcon(){
		return icon;
	}

	public String getName(){
		return name;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public String getVicinity(){
		return vicinity;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	public String getPlaceId(){
		return placeId;
	}

	public String getBusinessStatus(){
		return businessStatus;
	}

	public OpeningHours getOpeningHours(){
		return openingHours;
	}

	public PlusCode getPlusCode(){
		return plusCode;
	}

	public double getRating(){
		return rating;
	}

	public int getUserRatingsTotal(){
		return userRatingsTotal;
	}

	public boolean isPermanentlyClosed(){
		return permanentlyClosed;
	}
}