package object.nodes;

import com.google.gson.annotations.SerializedName;

public class ResponseNodes{

	@SerializedName("data")
	private DataNode dataNode;

	@SerializedName("status")
	private String status;

	public DataNode getData(){
		return dataNode;
	}

	public String getStatus(){
		return status;
	}
}