package object.result;

import com.google.gson.annotations.SerializedName;

public class ResponseAddPlace{

	@SerializedName("data")
	private DataAddPlace dataAddPlace;

	@SerializedName("status")
	private String status;

	public DataAddPlace getData(){
		return dataAddPlace;
	}

	public String getStatus(){
		return status;
	}
}