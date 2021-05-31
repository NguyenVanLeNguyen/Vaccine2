package object.places;

import com.google.gson.annotations.SerializedName;

public class OpeningHours{

	@SerializedName("open_now")
	private boolean openNow;

	public boolean isOpenNow(){
		return openNow;
	}
}