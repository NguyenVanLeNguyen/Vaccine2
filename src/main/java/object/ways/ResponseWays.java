package object.ways;

import com.google.gson.annotations.SerializedName;

public class ResponseWays{

	@SerializedName("data")
	private DataWays dataWays;

	@SerializedName("status")
	private String status;

	public void setData(DataWays dataWays){
		this.dataWays = dataWays;
	}

	public DataWays getData(){
		return dataWays;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}